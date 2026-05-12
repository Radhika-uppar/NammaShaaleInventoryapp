package com.example.nammashaale.di

import android.content.Context
import androidx.room.Room
import com.example.nammashaale.data.dao.db.AppDatabase
import com.example.nammashaale.data.dao.AssetDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "asset_db"
        ).fallbackToDestructiveMigration()
         .build()
    }

    @Provides
    fun provideDao(db: AppDatabase): AssetDao = db.assetDao()
}