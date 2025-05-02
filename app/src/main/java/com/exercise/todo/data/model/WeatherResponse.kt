package com.exercise.todo.data.model

import com.squareup.moshi.Json

data class WeatherResponse(
    val location: Location,
    val current: Current
)

data class Location(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    @Json(name = "tz_id") val tzId: String,
    @Json(name = "localtime_epoch") val localTimeEpoch: Long,
    val localtime: String
)

data class Current(
    @Json(name = "last_updated_epoch") val lastUpdatedEpoch: Long,
    @Json(name = "last_updated") val lastUpdated: String,
    @Json(name = "temp_c") val tempCelsius: Double,
    @Json(name = "temp_f") val tempFahrenheit: Double,
    @Json(name = "is_day") val isDay: Int,
    val condition: Condition
)

data class Condition(
    val text: String,
    val icon: String,
    val code: Int
)
