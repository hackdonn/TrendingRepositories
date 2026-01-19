package com.hackdon.jetpack.myapplication.data.remote.api

import com.hackdon.jetpack.myapplication.data.remote.dto.GitHubSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApi {
    @GET("search/repositories")
    suspend fun getTrendingRepos(
        @Query("q") query: String = "language:kotlin",
        @Query("sort") sort: String = "stars",
        @Query("order") order: String = "desc",
        @Query("per_page") perPage: Int = 30
    ): GitHubSearchResponseDto
}