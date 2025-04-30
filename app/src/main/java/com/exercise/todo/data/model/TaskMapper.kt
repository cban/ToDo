package com.exercise.todo.data.model

import com.exercise.todo.data.datasource.local.db.TaskEntity
import com.exercise.todo.data.model.Task

object TaskMapper {
    fun fromEntity(entity: TaskEntity): Task {
        return Task(
            title = entity.title,
            description = entity.description,
            isCompleted = entity.isCompleted
        )
    }
}