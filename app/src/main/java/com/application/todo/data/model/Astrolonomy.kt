package com.application.todo.data.model

import com.squareup.moshi.Json

data class AstronomyDto(
    val location: Location,
    val astronomy: Astronomy
)
data class Astronomy(
    val astro: Astro
)

data class Astro(
    val sunrise: String,
    val sunset: String,
    @Json(name = "moonrise")   val moonRise: String,
    @Json(name = "moonset")  val moonSet: String,
    @Json(name = "moon_phase") val moonPhase: String,
    @Json(name = "moon_illumination")  val moonIllumination: Int,
    @Json(name = "is_moon_up")   val isMoonUp: Int,
    @Json(name = "is_sun_up")    val isSunUp: Int
)
