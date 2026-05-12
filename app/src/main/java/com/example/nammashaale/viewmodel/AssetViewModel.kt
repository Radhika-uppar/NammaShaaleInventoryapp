package com.example.nammashaale.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nammashaale.data.dao.entity.Asset
import com.example.nammashaale.data.dao.entity.AssetHistory
import com.example.nammashaale.data.dao.repository.AssetRepository
import com.example.nammashaale.data.repository.FirestoreAssetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssetViewModel @Inject constructor(
    private val repo: AssetRepository,
    private val firestoreRepo: FirestoreAssetRepository
) : ViewModel() {

    val assetList = repo.getAllAssets()
    val recentActivity = repo.getRecentActivity()

    fun addAsset(name: String, serial: String, location: String = "Main Hall", category: String = "Electronics", notes: String = "", assetType: String = "New") {
        viewModelScope.launch {
            val asset = Asset(
                name = name,
                serialNumber = serial,
                location = location,
                category = category,
                condition = "GREEN",
                notes = notes,
                assetType = assetType
            )
            val newId = repo.insert(asset)
            
            // Insert history locally first
            repo.insertHistory(
                AssetHistory(
                    assetId = newId,
                    status = "GREEN",
                    adminName = "Admin",
                    message = "Added new $assetType asset: $name"
                )
            )

            // Sync to Firebase in background
            try {
                val savedAsset = asset.copy(id = newId)
                firestoreRepo.syncAsset(savedAsset)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateStatus(asset: Asset, status: String, adminName: String = "Admin", issueDescription: String = "") {
        viewModelScope.launch {
            val updatedAsset = asset.copy(condition = status)
            
            // 1. Update local asset
            repo.update(updatedAsset)
            
            // 2. Insert history immediately so it shows in UI
            val statusText = when(status) {
                "GREEN" -> "working"
                "YELLOW" -> "needs to repair"
                else -> "broken"
            }

            val message = if (status == "GREEN") {
                "Verified: ${asset.name} by $adminName"
            } else {
                "${asset.name} - ${if (issueDescription.isNotBlank()) issueDescription else "Asset status changed to $statusText"} by $adminName"
            }

            repo.insertHistory(
                AssetHistory(
                    assetId = asset.id,
                    status = status,
                    adminName = adminName,
                    message = message
                )
            )
            
            // 3. Sync update to Firebase (handled with try-catch to prevent blocking)
            try {
                firestoreRepo.syncAsset(updatedAsset)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun syncFromCloud() {
        viewModelScope.launch {
            val cloudAssets = firestoreRepo.fetchAllAssets()
            cloudAssets.forEach { asset ->
                repo.insert(asset)
            }
        }
    }

    fun getAssetHistory(assetId: Long): Flow<List<AssetHistory>> {
        return repo.getHistoryForAsset(assetId)
    }

    fun clearAllAssets() {
        viewModelScope.launch {
            repo.deleteAllAssets()
            repo.deleteAllHistory()
        }
    }
}
