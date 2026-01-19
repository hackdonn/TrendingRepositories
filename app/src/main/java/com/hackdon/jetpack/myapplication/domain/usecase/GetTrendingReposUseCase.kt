package com.hackdon.jetpack.myapplication.domain.usecase

import com.hackdon.jetpack.myapplication.domain.repository.RepoRepository
import javax.inject.Inject

class GetTrendingReposUseCase @Inject constructor(
    val repository: RepoRepository
) {
    suspend operator fun invoke() = repository.getTrendingRepos()
}