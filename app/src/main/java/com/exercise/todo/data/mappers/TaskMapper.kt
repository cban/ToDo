package com.exercise.todo.data.mappers

import com.exercise.todo.data.datasource.local.db.TaskEntity
import com.exercise.todo.data.model.Task

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