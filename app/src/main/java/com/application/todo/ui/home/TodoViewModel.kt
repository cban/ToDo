package com.application.todo.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.todo.data.model.CurrentWeather
import com.application.todo.data.model.Task
import com.application.todo.data.repository.TaskRepository
import com.application.todo.data.repository.WeatherRepository
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
class TodoViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    val tasksState: StateFlow<UiState<List<Task>>> = repository.getAllTasks()
        .map { tasks -> UiState.Success(tasks) as UiState<List<Task>> }
        .onStart { emit(UiState.Loading) }
        .catch { exception ->
            emit(UiState.Error(exception.message ?: "Unknown error"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = UiState.Idle
        )


    private val _weatherState = MutableStateFlow<UiState<CurrentWeather>?>(UiState.Idle)
    val weatherState = _weatherState.asStateFlow()

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
                repository.addTask(task)
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

    fun getWeather() {
        viewModelScope.launch {
            try {
                _weatherState.update { UiState.Loading }
                val location = weatherRepository.getLocation()
                location?.let {
                    val weatherData =
                        weatherRepository.getCurrentWeather("${it.latitude},${it.longitude}")
                    weatherData?.let { weather ->
                        _weatherState.update { UiState.Success(weather) }
                    } ?: run {
                        _weatherState.update { UiState.Error("Weather data not found") }
                    }
                }
            } catch (e: Exception) {
                _weatherState.update { UiState.Error(e.message.toString()) }
            }
        }
    }
}