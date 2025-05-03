package com.application.todo.data.repository


import android.location.Location
import com.application.todo.data.datasource.LocationDataSource
import com.application.todo.data.datasource.network.WeatherDataSource
import com.application.todo.data.mappers.toDomain
import com.application.todo.data.model.CurrentWeather
import javax.inject.Inject


class WeatherRepositoryImpl @Inject constructor(
    private val weatherDataSource: WeatherDataSource,
    private val locationDataSource: LocationDataSource
) : WeatherRepository {

    override suspend fun getLocation(): Location? {
        return try {
            locationDataSource.getDeviceLocation()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getCurrentWeather(location: String): CurrentWeather? {
        return try {
            val weatherData = weatherDataSource.getCurrentWeather(location)
            val astronomyData = weatherDataSource.getWeatherAstronomy(location)
            if (weatherData != null && astronomyData != null) {
                weatherData.toDomain(astronomyData)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }

    }


}