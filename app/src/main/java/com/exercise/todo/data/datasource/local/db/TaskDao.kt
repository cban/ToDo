package com.exercise.todo.data.datasource.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Upsert
    suspend fun updateTask(task: TaskEntity)

    @Query("SELECT * FROM task")
     fun getAllTasks(): Flow<List<TaskEntity>>


    @Query("SELECT * FROM task WHERE isCompleted = 1")
    suspend fun getCompletedTasks(): List<TaskEntity>

    @Query("SELECT * FROM task WHERE isCompleted = 0")
    suspend fun getUncompletedTasks(): List<TaskEntity>

    @Query("DELETE FROM task WHERE id = :taskId")
    suspend fun deleteTask(taskId: Int)
}