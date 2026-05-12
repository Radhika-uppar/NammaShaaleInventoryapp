package com.example.nammashaale.data.dao.repository

import com.example.nammashaale.data.dao.AssetDao
import com.example.nammashaale.data.dao.entity.Asset
import com.example.nammashaale.data.dao.entity.AssetHistory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AssetRepository @Inject constructor(
    private val dao: AssetDao
) {

    fun getAllAssets(): Flow<List<Asset>> {
        return dao.getAllAssets()
    }

    suspend fun insert(asset: Asset): Long {
        return dao.insertAsset(asset)
    }

    suspend fun update(asset: Asset) {
        dao.updateAsset(asset)
    }

    fun getHistoryForAsset(assetId: Long): Flow<List<AssetHistory>> {
        return dao.getHistoryForAsset(assetId)
    }

    suspend fun insertHistory(history: AssetHistory) {
        dao.insertHistory(history)
    }

    fun getRecentActivity(): Flow<List<AssetHistory>> {
        return dao.getRecentActivity()
    }

    suspend fun deleteAllAssets() {
        dao.deleteAllAssets()
    }

    suspend fun deleteAllHistory() {
        dao.deleteAllHistory()
    }
}
