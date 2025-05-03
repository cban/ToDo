package com.application.todo.di

import android.content.Context
import androidx.room.Room
import com.application.todo.data.datasource.TaskDataSource
import com.application.todo.data.datasource.local.LocalDataSourceImpl
import com.application.todo.data.datasource.local.db.TaskDao
import com.application.todo.data.datasource.local.db.TodoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideTodoDatabase(@ApplicationContext context: Context): TodoDatabase {
        return Room.databaseBuilder(
            context,
            TodoDatabase::class.java,
            "todo_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideTaskDao(database: TodoDatabase): TaskDao {
        return database.taskDao()
    }

    @Provides
    fun provideLocalDataSource(
        taskDao: TaskDao
    ): TaskDataSource {
        return LocalDataSourceImpl(taskDao)
    }
}