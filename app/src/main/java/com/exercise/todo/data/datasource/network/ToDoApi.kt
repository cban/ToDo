package com.exercise.todo.data.datasource.network

import com.exercise.todo.data.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ToDoApi {

    @GET("current.json")
    suspend fun getCurrentWeather(@Query("q") location: String): Response<WeatherResponse>
}