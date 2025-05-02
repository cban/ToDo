package com.exercise.todo.data.datasource

import com.exercise.todo.data.model.WeatherResponse

interface WeatherDataSource {
    suspend fun getCurrentWeather(location: String): WeatherResponse?
}