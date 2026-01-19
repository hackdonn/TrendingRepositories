package com.hackdon.jetpack.myapplication.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackdon.jetpack.myapplication.domain.model.RepoListState
import com.hackdon.jetpack.myapplication.domain.usecase.GetTrendingReposUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoListViewModel @Inject constructor(
    private val useCase: GetTrendingReposUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<RepoListState>(RepoListState.Loading)
    val state: StateFlow<RepoListState> = _state.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var allRepositories: List<com.hackdon.jetpack.myapplication.domain.model.Repository> = emptyList()

    init {
        fetchRepos()
        observeSearchQuery()
    }

    private fun observeSearchQuery() {
        _searchQuery
            .debounce(300)
            .onEach { query ->
                filterRepositories(query)
            }
            .launchIn(viewModelScope)
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onRefresh() {
        fetchRepos()
    }

    private fun fetchRepos() = viewModelScope.launch {
        _state.value = RepoListState.Loading
        try {
            val result = useCase()
            if (result.isSuccess) {
                allRepositories = result.getOrNull() ?: emptyList()
                filterRepositories(_searchQuery.value)
            } else {
                _state.value = RepoListState.Error(
                    result.exceptionOrNull()?.message ?: "Unknown error occurred"
                )
            }
        } catch (e: Exception) {
            _state.value = RepoListState.Error(e.message ?: "Unknown error")
        }
    }

    private fun filterRepositories(query: String) {
        if (allRepositories.isEmpty()) {
            return
        }

        val filtered = if (query.isBlank()) {
            allRepositories
        } else {
            allRepositories.filter { repo ->
                repo.name.contains(query, ignoreCase = true) ||
                        repo.fullName.contains(query, ignoreCase = true) ||
                        repo.description?.contains(query, ignoreCase = true) == true
            }
        }

        _state.value = when {
            filtered.isEmpty() -> RepoListState.Empty
            else -> RepoListState.Success(filtered)
        }
    }

    fun retry() {
        fetchRepos()
    }
}