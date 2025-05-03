package com.application.todo.data.datasource.network

import com.application.todo.data.model.AstronomyDto
import com.application.todo.data.model.WeatherResponse

interface WeatherDataSource {
    suspend fun getCurrentWeather(location: String): WeatherResponse?
    suspend fun getWeatherAstronomy(location: String): AstronomyDto?

}