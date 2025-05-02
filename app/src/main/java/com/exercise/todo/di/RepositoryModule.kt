package com.exercise.todo.di

import com.exercise.todo.data.datasource.TaskDataSource
import com.exercise.todo.data.repository.TaskRepository
import com.exercise.todo.data.repository.TaskRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideTaskRepository(
        localDataSource: TaskDataSource
    ): TaskRepository {
        return TaskRepositoryImpl(localDataSource)
    }

}