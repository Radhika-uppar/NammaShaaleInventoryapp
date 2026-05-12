package com.example.nammashaale.data.dao.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assets")
data class Asset(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val serialNumber: String,
    val name: String,
    val category: String = "Electronics",
    val location: String = "Main Hall",
    val condition: String = "GREEN", // GREEN=Working, YELLOW=Needs Repair, RED=Broken
    val notes: String = "",
    val lastAuditor: String = "Admin",
    val assetType: String = "New" // New or Used
)

@Entity(tableName = "asset_history")
data class AssetHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val assetId: Long,
    val status: String,
    val timestamp: Long = System.currentTimeMillis(),
    val adminName: String,
    val message: String
)
