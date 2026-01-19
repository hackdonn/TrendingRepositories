package com.hackdon.jetpack.myapplication.navigation

import android.util.Base64
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hackdon.jetpack.myapplication.presentation.detail.RepoDetailScreen
import com.hackdon.jetpack.myapplication.presentation.detail.RepoDetailViewModel
import com.hackdon.jetpack.myapplication.presentation.list.RepoListScreen
import com.hackdon.jetpack.myapplication.presentation.list.RepoListViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

sealed class Screen(val route: String) {
    object RepoList : Screen("repo_list")
    object RepoDetail : Screen("repo_detail")
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.RepoList.route,
        modifier = modifier
    ) {
        composable(route = Screen.RepoList.route) {
            val viewModel: RepoListViewModel = hiltViewModel()
            RepoListScreen(
                viewModel = viewModel,
                onRepoClick = { repository ->
                    val repositoryJson = Json.encodeToString(repository)
                    val encodedJson = Base64.encodeToString(
                        repositoryJson.toByteArray(Charsets.UTF_8),
                        Base64.URL_SAFE or Base64.NO_WRAP
                    )
                    navController.navigate("${Screen.RepoDetail.route}/$encodedJson")
                }
            )
        }

        composable(
            route = "${Screen.RepoDetail.route}/{repositoryJson}",
            arguments = listOf(
                navArgument("repositoryJson") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val encodedJson = backStackEntry.arguments?.getString("repositoryJson") ?: ""

            val repository = try {
                val decodedJson = Base64.decode(
                    encodedJson,
                    Base64.URL_SAFE or Base64.NO_WRAP
                ).toString(Charsets.UTF_8)
                Json.decodeFromString<com.hackdon.jetpack.myapplication.domain.model.Repository>(
                    decodedJson
                )
            } catch (e: Exception) {
                null
            }

            repository?.let { repo ->
                val viewModel: RepoDetailViewModel = viewModel(
                    factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return RepoDetailViewModel(repo) as T
                        }
                    }
                )
                RepoDetailScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}