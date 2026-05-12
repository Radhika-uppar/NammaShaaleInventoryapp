package com.example.nammashaale.data.dao.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nammashaale.data.dao.AssetDao
import com.example.nammashaale.data.dao.entity.Asset
import com.example.nammashaale.data.dao.entity.AssetHistory

@Database(entities = [Asset::class, AssetHistory::class], version = 8)
abstract class AppDatabase : RoomDatabase() {
    abstract fun assetDao(): AssetDao
}