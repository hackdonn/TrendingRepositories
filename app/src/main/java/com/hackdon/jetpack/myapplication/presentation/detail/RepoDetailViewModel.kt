package com.hackdon.jetpack.myapplication.presentation.detail

import androidx.lifecycle.ViewModel
import com.hackdon.jetpack.myapplication.domain.model.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RepoDetailViewModel(
    repository: Repository
) : ViewModel() {

    private val _repository = MutableStateFlow(repository)
    val repository: StateFlow<Repository?> = _repository.asStateFlow()

    companion object {
        fun create(repository: Repository) = RepoDetailViewModel(repository)
    }
}