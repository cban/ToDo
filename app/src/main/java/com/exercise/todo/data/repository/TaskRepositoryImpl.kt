package com.exercise.todo.data.repository

import com.exercise.todo.data.datasource.TaskDataSource
import com.exercise.todo.data.model.Task
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val localDataSource: TaskDataSource
) :
    TaskRepository {
    override suspend fun getAllTasks(): List<Task> {
        return try {
            localDataSource.getAllTasks()
        } catch (e: Exception) {
            emptyList()
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