package com.application.todo.data.datasource

import com.application.todo.data.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskDataSource {
    fun getAllTasks(): Flow<List<Task>>
    suspend fun getCompletedTasks(): List<Task>
    suspend fun getUncompletedTasks(): List<Task>
    suspend fun insertTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(taskId: Int)
}