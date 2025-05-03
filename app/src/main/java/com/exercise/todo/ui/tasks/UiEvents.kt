package com.exercise.todo.ui.tasks

import com.exercise.todo.data.model.Task

sealed class UiEvents{
    data class DeleteTask(val taskId: Int): UiEvents()
    data class AddTask(val task: Task): UiEvents()
    data class UpdateTask(val task: Task): UiEvents()
}
