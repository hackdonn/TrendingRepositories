package com.hackdon.jetpack.myapplication.data.remote.dto

data class GitHubSearchResponseDto(
    val items: List<RepositoryDto>
)

data class RepositoryDto(
    val id: Long,
    val name: String,
    val full_name: String,
    val owner: OwnerDto,
    val html_url: String,
    val description: String?,
    val stargazers_count: Int,
    val forks_count: Int,
    val watchers_count: Int,
    val language: String?,
    val created_at: String
)

data class OwnerDto(
    val login: String,
    val avatar_url: String
)

fun RepositoryDto.toDomainModel() = com.hackdon.jetpack.myapplication.domain.model.Repository(
    id = id,
    name = name,
    fullName = full_name,
    owner = com.hackdon.jetpack.myapplication.domain.model.Repository.Owner(
        login = owner.login,
        avatarUrl = owner.avatar_url
    ),
    htmlUrl = html_url,
    description = description,
    stargazersCount = stargazers_count,
    forksCount = forks_count,
    watchersCount = watchers_count,
    language = language,
    createdAt = created_at
)