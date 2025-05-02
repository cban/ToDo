package com.exercise.todo.data.datasource

import com.exercise.todo.data.model.Task

interface TaskDataSource {
    suspend fun getAllTasks(): List<Task>
    suspend fun getCompletedTasks(): List<Task>
    suspend fun getUncompletedTasks(): List<Task>
    suspend fun insertTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(taskId: Int)
}