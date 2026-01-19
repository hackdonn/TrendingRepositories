package com.hackdon.jetpack.myapplication.domain.model

import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

@Serializable
data class Repository(
    val id: Long,
    val name: String,
    val fullName: String,
    val owner: Owner,
    val htmlUrl: String,
    val description: String?,
    val stargazersCount: Int,
    val forksCount: Int,
    val watchersCount: Int,
    val language: String?,
    val createdAt: String
) {
    @Serializable
    data class Owner(
        val login: String,
        val avatarUrl: String
    )

    val formattedStars: String
        get() = formatNumber(stargazersCount)

    val formattedForks: String
        get() = formatNumber(forksCount)

    val formattedWatchers: String
        get() = formatNumber(watchersCount)

    val formattedDate: String
        get() {
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
                val outputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.US)
                val date = inputFormat.parse(createdAt) ?: return createdAt
                outputFormat.format(date)
            } catch (e: Exception) {
                createdAt
            }
        }

    private fun formatNumber(number: Int): String {
        return when {
            number >= 1_000_000 -> String.format(Locale.US, "%.1fM", number / 1_000_000.0)
            number >= 1_000 -> String.format(Locale.US, "%.1fK", number / 1_000.0)
            else -> number.toString()
        }
    }
}

sealed class RepoListState {
    object Loading : RepoListState()
    data class Success(val repos: List<Repository>) : RepoListState()
    data class Error(val message: String) : RepoListState()
    object Empty : RepoListState()
}