package com.hackdon.jetpack.myapplication.domain.repository

import com.hackdon.jetpack.myapplication.domain.model.Repository

interface RepoRepository {
    suspend fun getTrendingRepos(): Result<List<Repository>>
}