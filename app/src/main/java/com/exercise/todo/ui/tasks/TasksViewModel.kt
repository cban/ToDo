package com.exercise.todo.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exercise.todo.data.model.Task
import com.exercise.todo.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val repository: TaskRepository,
) : ViewModel() {
    private val _tasksState = MutableStateFlow<UiState>(UiState.Idle)
    val tasksState = _tasksState.asStateFlow()

    init {
        getAllTasks()
    }

    fun handleEvent(event: UiEvents) {
        when (event) {
            is UiEvents.LoadTasks -> getAllTasks()
            is UiEvents.LoadCompletedTasks -> getCompletedTasks()
            is UiEvents.DeleteTask -> deleteTask(event.taskId)
            is UiEvents.AddTask -> addTask(event.task)
            is UiEvents.UpdateTask -> updateTask(event.task)
        }
    }

    private fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                _tasksState.update { UiState.Loading }
                repository.updateTask(task)
                val response = repository.getAllTasks()
                _tasksState.update { UiState.Success(response) }
            } catch (e: Exception) {
                _tasksState.update { UiState.Error(e.message.toString()) }

            }
        }
    }

    fun getAllTasks() {
        viewModelScope.launch {
            try {
                _tasksState.update { UiState.Loading }
                val response = repository.getAllTasks()
                _tasksState.update { UiState.Success(response) }
            } catch (e: Exception) {
                _tasksState.update { UiState.Error(e.message.toString()) }
            }
        }
    }

    fun getCompletedTasks() {
        viewModelScope.launch {
            try {
                _tasksState.update { UiState.Loading }
                val response = repository.getCompletedTasks()

                _tasksState.update { UiState.Success(response) }
            } catch (e: Exception) {
                _tasksState.update { UiState.Error(e.message.toString()) }
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            try {
                _tasksState.update { UiState.Loading }
                repository.insertTask(task)
                val response = repository.getAllTasks()
                _tasksState.update { UiState.Success(response) }
            } catch (e: Exception) {
                _tasksState.update { UiState.Error(e.message.toString()) }
            }
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            try {
                _tasksState.update { UiState.Loading }
                repository.deleteTask(taskId)
                val response = repository.getAllTasks()
                _tasksState.update { UiState.Success(response) }
            } catch (e: Exception) {
                _tasksState.update { UiState.Error(e.message.toString()) }
            }
        }
    }

}