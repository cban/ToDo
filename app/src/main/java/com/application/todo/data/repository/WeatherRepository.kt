package com.application.todo.data.repository

import android.location.Location
import com.application.todo.data.model.CurrentWeather

interface WeatherRepository {
    suspend fun getLocation(): Location?
    suspend fun getCurrentWeather(location: String): CurrentWeather?

}