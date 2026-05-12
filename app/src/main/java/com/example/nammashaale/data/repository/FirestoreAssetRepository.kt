package com.example.nammashaale.data.repository

import com.example.nammashaale.data.dao.entity.Asset
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreAssetRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private val userId: String? get() = auth.currentUser?.uid
    private val assetsCollection get() = userId?.let { firestore.collection("users").document(it).collection("assets") }

    suspend fun syncAsset(asset: Asset) {
        assetsCollection?.document(asset.id.toString())?.set(asset)?.await()
    }

    suspend fun deleteAsset(assetId: Long) {
        assetsCollection?.document(assetId.toString())?.delete()?.await()
    }

    suspend fun fetchAllAssets(): List<Asset> {
        return assetsCollection?.get()?.await()?.toObjects(Asset::class.java) ?: emptyList()
    }
}
