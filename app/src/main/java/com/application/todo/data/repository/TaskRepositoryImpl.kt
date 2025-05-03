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
    override  fun getAllTasks(): Flow<List<Task>> {
        return try {
            localDataSource.getAllTasks()
        } catch (e: Exception) {
            emptyFlow()
        }
    }

    override suspend fun getUncompletedTasks(): List<Task> {
       return try {
           localDataSource.getUncompletedTasks()
       } catch (e: Exception){
              emptyList()
       }
    }

    override suspend fun getCompletedTasks(): List<Task> {
        return try {
            localDataSource.getCompletedTasks()
        } catch (e: Exception) {
            emptyList()
        }
    }


    override suspend fun insertTask(task: Task) {
        try {
            localDataSource.insertTask(task)
        } catch (e: Exception) {
            //
        }
    }

    override suspend fun updateTask(task: Task) {
        try {
            localDataSource.updateTask(task)
        } catch (e: Exception) {

        }
    }

    override suspend fun deleteTask(taskId: Int) {
        try {
            localDataSource.deleteTask(taskId)
        } catch (e: Exception) {

        }
    }
}