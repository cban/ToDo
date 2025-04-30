package com.exercise.todo.data.datasource.network

import retrofit2.http.GET
import retrofit2.http.Query

interface ToDoApi {

    @GET("current.json")
    suspend fun getCurrentWeather(@Query("q") location: String)
}