package com.application.todo.data.datasource.local

import com.application.todo.data.datasource.TaskDataSource
import com.application.todo.data.datasource.local.db.TaskDao
import com.application.todo.data.mappers.toDomain
import com.application.todo.data.mappers.toEntity
import com.application.todo.data.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskDataSource {

    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { it.map { it.toDomain() } }
    }

    override suspend fun getCompletedTasks(): List<Task> {
        return taskDao.getCompletedTasks().map { it.toDomain() }
    }

    override suspend fun getUncompletedTasks(): List<Task> {
        return taskDao.getUncompletedTasks().map { it.toDomain() }
    }

    override suspend fun insertTask(task: Task) {
        taskDao.insertTask(task.toEntity())
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity(task.id))
    }

    override suspend fun deleteTask(taskId: Int) {
        taskDao.deleteTask(taskId)
    }

}