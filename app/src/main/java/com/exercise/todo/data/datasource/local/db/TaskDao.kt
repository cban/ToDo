package com.exercise.todo.data.datasource.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Query("SELECT * FROM task")
    suspend fun getAllTasks(): List<TaskEntity>

    @Query("SELECT * FROM task WHERE isCompleted = 1")
    suspend fun getCompletedTasks(): List<TaskEntity>

    @Query("SELECT * FROM task WHERE isCompleted = 0")
    suspend fun getUncompletedTasks(): List<TaskEntity>

    @Delete
    suspend fun deleteTask(task: TaskEntity)
}