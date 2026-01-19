package com.hackdon.jetpack.myapplication.data.repository

import com.hackdon.jetpack.myapplication.data.remote.api.GitHubApi
import com.hackdon.jetpack.myapplication.data.remote.dto.toDomainModel
import com.hackdon.jetpack.myapplication.domain.model.Repository
import com.hackdon.jetpack.myapplication.domain.repository.RepoRepository
import javax.inject.Inject

class RepoRepositoryImpl @Inject constructor(
    private val api: GitHubApi
) : RepoRepository {

    override suspend fun getTrendingRepos(): Result<List<Repository>> {
        return try {
            val response = api.getTrendingRepos()
            val repos = response.items.map { it.toDomainModel() }
            Result.success(repos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}