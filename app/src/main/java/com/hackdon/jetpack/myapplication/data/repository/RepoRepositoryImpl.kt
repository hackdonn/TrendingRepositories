package com.hackdon.jetpack.myapplication.data.repository

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.hackdon.jetpack.myapplication.data.local.LocalDataSource
import com.hackdon.jetpack.myapplication.data.remote.api.GitHubApi
import com.hackdon.jetpack.myapplication.data.remote.dto.toDomainModel
import com.hackdon.jetpack.myapplication.domain.model.Repository
import com.hackdon.jetpack.myapplication.domain.repository.RepoRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class RepoRepositoryImpl @Inject constructor(
    private val api: GitHubApi,
    private val localDataSource: LocalDataSource,
    private val connectivityManager: ConnectivityManager
) : RepoRepository {

    override suspend fun getTrendingRepos(): Result<List<Repository>> {
        return try {
            val response = api.getTrendingRepos()
            val repos = response.items.map { it.toDomainModel() }
            // Cache successful fetch
            localDataSource.cacheRepositories(repos)
            Result.success(repos)
        } catch (e: Exception) {
            // Network error, try to get cached data
            if (isNetworkAvailable()) {
                // Got error but network available - could be server error
                Result.failure(e)
            } else {
                // Network not available, try cache
                val cachedRepos = localDataSource.getCachedRepositories().firstOrNull()

                if (cachedRepos != null && cachedRepos.isNotEmpty()) {
                    Result.success(cachedRepos)
                } else {
                    Result.failure(Exception("No internet connection and no cached data available"))
                }
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}