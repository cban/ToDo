package com.application.todo.data.datasource.network

import com.application.todo.data.model.AstronomyDto
import com.application.todo.data.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("current.json")
    suspend fun getCurrentWeather(@Query("q") location: String): Response<WeatherResponse>

    @GET("astronomy.json")
    suspend fun getWeatherAstronomy(@Query("q") location: String): Response<AstronomyDto>
}