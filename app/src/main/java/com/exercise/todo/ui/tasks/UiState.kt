package com.exercise.todo.ui.tasks

import com.exercise.todo.data.model.Task

sealed class UiState {
    object Loading : UiState()
    object Idle : UiState()
    data class Success(val tasks: List<Task>?) : UiState()
    data class Error(val message: String) : UiState()

}