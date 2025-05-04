package com.application.todo.data.repository

import com.application.todo.data.datasource.TaskDataSource
import com.application.todo.data.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val localDataSource: TaskDataSource
) :
    TaskRepository {
    override fun getAllTasks(): Flow<List<Task>> {
        return try {
            localDataSource.getAllTasks()
        } catch (e: Exception) {
            emptyFlow()
        }
    }


    override suspend fun addTask(task: Task) {
        localDataSource.insertTask(task)
    }

    override suspend fun updateTask(task: Task) {
        localDataSource.updateTask(task)
    }

    override suspend fun deleteTask(taskId: Int) {
        localDataSource.deleteTask(taskId)
    }
}