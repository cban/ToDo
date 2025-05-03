package com.exercise.todo.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exercise.todo.data.model.Task
import com.exercise.todo.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val repository: TaskRepository,
) : ViewModel() {

    val tasksState: StateFlow<UiState> = repository.getAllTasks()
        .map<List<Task>, UiState> { UiState.Success(it) }
        .onStart { emit(UiState.Loading) }
        .catch { emit(UiState.Error(it.message ?: "Unknown error")) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Idle)

    private val _errorState = MutableStateFlow<UiState.Error>(UiState.Error(""))
    val errorState = _errorState.asStateFlow()

    fun handleEvent(event: UiEvents) {
        when (event) {
            is UiEvents.DeleteTask -> deleteTask(event.taskId)
            is UiEvents.AddTask -> addTask(event.task)
            is UiEvents.UpdateTask -> updateTask(event.task)
        }
    }

    fun getFilteredTasks(tasks: List<Task>?, isCompleted: Boolean): List<Task> {
        return tasks?.filter { it.isCompleted == isCompleted } ?: emptyList()
    }

    private fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                repository.updateTask(task)
            } catch (e: Exception) {
                _errorState.update { UiState.Error(e.message.toString()) }

            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            try {
                repository.insertTask(task)
            } catch (e: Exception) {
                _errorState.update { UiState.Error(e.message.toString()) }
            }
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            try {
                repository.deleteTask(taskId)
            } catch (e: Exception) {
                _errorState.update { UiState.Error(e.message.toString()) }
            }
        }
    }
}