package com.example.nammashaale.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.nammashaale.data.dao.entity.Asset
import com.example.nammashaale.data.dao.entity.AssetHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface AssetDao {

    @Query("SELECT * FROM assets")
    fun getAllAssets(): Flow<List<Asset>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsset(asset: Asset): Long

    @Update
    suspend fun updateAsset(asset: Asset)

    @Query("SELECT * FROM asset_history WHERE assetId = :assetId ORDER BY timestamp DESC")
    fun getHistoryForAsset(assetId: Long): Flow<List<AssetHistory>>

    @Insert
    suspend fun insertHistory(history: AssetHistory)

    @Query("SELECT * FROM asset_history ORDER BY timestamp DESC LIMIT 20")
    fun getRecentActivity(): Flow<List<AssetHistory>>

    @Query("DELETE FROM assets")
    suspend fun deleteAllAssets()

    @Query("DELETE FROM asset_history")
    suspend fun deleteAllHistory()
}