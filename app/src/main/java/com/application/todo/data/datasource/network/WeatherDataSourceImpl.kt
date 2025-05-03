package com.application.todo.data.datasource.network

import com.application.todo.data.model.AstronomyDto
import com.application.todo.data.model.WeatherResponse
import javax.inject.Inject

class WeatherDataSourceImpl @Inject constructor(private val api: WeatherApi): WeatherDataSource {
    override suspend fun getCurrentWeather(location: String): WeatherResponse? {
        val response = api.getCurrentWeather(location)
        if (response.isSuccessful) {
            return response.body()
        } else {
            throw Exception("Error fetching weather data")
        }
    }

    override suspend fun getWeatherAstronomy(location: String): AstronomyDto? {
        val response = api.getWeatherAstronomy(location)
        if (response.isSuccessful) {
            return response.body()
        } else {
            throw Exception("Error fetching weather data")
        }
    }
}