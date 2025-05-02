package com.exercise.todo.data.datasource.local

import com.exercise.todo.data.datasource.TaskDataSource
import com.exercise.todo.data.datasource.local.db.TaskDao
import com.exercise.todo.data.mappers.toDomain
import com.exercise.todo.data.mappers.toEntity
import com.exercise.todo.data.model.Task
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskDataSource {

    override suspend fun getAllTasks(): List<Task> {
        return taskDao.getAllTasks().map { it.toDomain() }
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