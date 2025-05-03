package com.application.todo.di

import com.application.todo.data.datasource.LocationDataSource
import com.application.todo.data.datasource.TaskDataSource
import com.application.todo.data.datasource.network.WeatherDataSource
import com.application.todo.data.repository.TaskRepository
import com.application.todo.data.repository.TaskRepositoryImpl
import com.application.todo.data.repository.WeatherRepository
import com.application.todo.data.repository.WeatherRepositoryImpl
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


    @Provides
    fun provideWeatherRepository(
        weatherDataSource: WeatherDataSource,
        locationDataSource: LocationDataSource
    ): WeatherRepository {
        return WeatherRepositoryImpl(weatherDataSource, locationDataSource = locationDataSource)
    }

}