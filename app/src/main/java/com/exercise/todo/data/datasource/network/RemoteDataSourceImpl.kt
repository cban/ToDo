package com.exercise.todo.data.datasource.network

import com.exercise.todo.data.model.WeatherResponse
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val api: ToDoApi): RemoteDataSource {
    override suspend fun getCurrentWeather(location: String): WeatherResponse? {
        val response = api.getCurrentWeather(location)
        if (response.isSuccessful) {
            return response.body()
        } else {
            throw Exception("Error fetching weather data")
        }
    }
}