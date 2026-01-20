package com.hackdon.jetpack.myapplication.data.local

import com.hackdon.jetpack.myapplication.data.local.entity.RepositoryEntity
import com.hackdon.jetpack.myapplication.domain.model.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
    private val database: AppDatabase
) {

    companion object {
        private const val CACHE_EXPIRY_MS = 24 * 60 * 60 * 1000 // 24 hours
    }

    fun getCachedRepositories(): Flow<List<Repository>> {
        return database.repositoryDao().getAllRepositories()
            .map { entities ->
                entities.map { RepositoryEntity.toDomainModel(it) }
            }
    }

    suspend fun clearCachedRepositories() {
        database.repositoryDao().clearAllRepositories()
    }

    suspend fun cacheRepositories(repositories: List<Repository>) {
        database.repositoryDao().clearAllRepositories()
        database.repositoryDao()
            .insertRepositories(repositories.map { RepositoryEntity.toEntity(it) })
    }

    suspend fun hasCachedData(): Boolean {
        return database.repositoryDao().getRepositoryCount() > 0 && !isCacheExpired()
    }

    suspend fun isCacheExpired(): Boolean {
        val firstRepo = database.repositoryDao().getFirstRepository() ?: return true
        return System.currentTimeMillis() - firstRepo.cachedAt > CACHE_EXPIRY_MS
    }
}