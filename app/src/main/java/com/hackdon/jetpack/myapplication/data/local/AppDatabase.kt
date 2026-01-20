package com.hackdon.jetpack.myapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hackdon.jetpack.myapplication.data.local.dao.RepositoryDao
import com.hackdon.jetpack.myapplication.data.local.entity.RepositoryEntity

@Database(
    entities = [RepositoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun repositoryDao(): RepositoryDao
}