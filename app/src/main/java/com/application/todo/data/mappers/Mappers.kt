package com.application.todo.data.mappers

import com.application.todo.data.datasource.local.db.TaskEntity
import com.application.todo.data.model.AstronomyDto
import com.application.todo.data.model.CurrentWeather
import com.application.todo.data.model.Task
import com.application.todo.data.model.WeatherResponse

fun Task.toEntity(id: Int = 0): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
    )
}

fun TaskEntity.toDomain(): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted
    )
}

fun WeatherResponse.toDomain(astronomy: AstronomyDto): CurrentWeather {
    return CurrentWeather(
        location = location.name,
        temperature = current.tempCelsius,
        condition = current.condition.text,
        icon = current.condition.icon,
        sunriseTime = astronomy.astronomy.astro.sunrise,
        sunsetTime = astronomy.astronomy.astro.sunset,
    )
}