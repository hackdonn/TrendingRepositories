package com.hackdon.jetpack.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repositories")
data class RepositoryEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val fullName: String,
    val ownerLogin: String,
    val ownerAvatarUrl: String,
    val htmlUrl: String,
    val description: String?,
    val stargazersCount: Int,
    val forksCount: Int,
    val watchersCount: Int,
    val language: String?,
    val createdAt: String,
    val cachedAt: Long = System.currentTimeMillis()
) {
    companion object {
        fun toDomainModel(entity: RepositoryEntity) =
            com.hackdon.jetpack.myapplication.domain.model.Repository(
                id = entity.id,
                name = entity.name,
                fullName = entity.fullName,
                owner = com.hackdon.jetpack.myapplication.domain.model.Repository.Owner(
                    login = entity.ownerLogin,
                    avatarUrl = entity.ownerAvatarUrl
                ),
                htmlUrl = entity.htmlUrl,
                description = entity.description,
                stargazersCount = entity.stargazersCount,
                forksCount = entity.forksCount,
                watchersCount = entity.watchersCount,
                language = entity.language,
                createdAt = entity.createdAt
            )

        fun toEntity(repository: com.hackdon.jetpack.myapplication.domain.model.Repository) =
            RepositoryEntity(
                id = repository.id,
                name = repository.name,
                fullName = repository.fullName,
                ownerLogin = repository.owner.login,
                ownerAvatarUrl = repository.owner.avatarUrl,
                htmlUrl = repository.htmlUrl,
                description = repository.description,
                stargazersCount = repository.stargazersCount,
                forksCount = repository.forksCount,
                watchersCount = repository.watchersCount,
                language = repository.language,
                createdAt = repository.createdAt
            )
    }
}