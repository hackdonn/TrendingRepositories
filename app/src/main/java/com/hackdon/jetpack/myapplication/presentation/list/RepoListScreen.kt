package com.hackdon.jetpack.myapplication.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hackdon.jetpack.myapplication.domain.model.RepoListState
import com.hackdon.jetpack.myapplication.domain.model.Repository
import com.hackdon.jetpack.myapplication.presentation.components.EmptyView
import com.hackdon.jetpack.myapplication.presentation.components.ErrorView
import com.hackdon.jetpack.myapplication.presentation.components.LoadingView
import com.hackdon.jetpack.myapplication.presentation.components.RepoListItem
import com.hackdon.jetpack.myapplication.presentation.components.RepositorySearchBar
import kotlinx.coroutines.launch

@Composable
fun RepoListScreen(
    viewModel: RepoListViewModel,
    onRepoClick: (Repository) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(state) {
        if (state is RepoListState.Error) {
            coroutineScope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = "Failed to load repositories",
                    actionLabel = "Retry",
                    duration = SnackbarDuration.Long
                )
                if (result == SnackbarResult.ActionPerformed) {
                    viewModel.retry()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            SearchSection(
                query = searchQuery,
                onQueryChange = viewModel::onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            PullToRefreshBox(
                isRefreshing = state is RepoListState.Loading && !searchQuery.isBlank(),
                onRefresh = { viewModel.onRefresh() },
                state = pullToRefreshState,
                modifier = Modifier.fillMaxSize()
            ) {
                when (state) {
                    is RepoListState.Loading -> {
                        LoadingView()
                    }

                    is RepoListState.Success -> {
                        val repos = (state as RepoListState.Success).repos
                        RepoListContent(
                            repos = repos,
                            onItemClick = onRepoClick
                        )
                    }

                    is RepoListState.Error -> {
                        val errorMessage = (state as RepoListState.Error).message
                        ErrorView(
                            message = errorMessage,
                            isNetworkError = errorMessage.contains("network", ignoreCase = true) ||
                                    errorMessage.contains("connection", ignoreCase = true),
                            onRetry = { viewModel.retry() }
                        )
                    }

                    is RepoListState.Empty -> {
                        EmptyView()
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchSection(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        RepositorySearchBar(
            query = query,
            onQueryChange = onQueryChange
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun RepoListContent(
    repos: List<Repository>,
    onItemClick: (Repository) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = repos,
            key = { it.id }
        ) { repo ->
            RepoListItem(
                repository = repo,
                onClick = { onItemClick(repo) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
    }
}