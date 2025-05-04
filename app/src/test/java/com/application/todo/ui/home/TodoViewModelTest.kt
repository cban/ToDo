package com.application.todo.ui.home

import com.application.todo.data.model.Task
import com.application.todo.data.repository.TaskRepository
import com.application.todo.data.repository.WeatherRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


@ExperimentalCoroutinesApi
class TodoViewModelTest {

    private lateinit var subjectUnderTest: TodoViewModel
    private val repository = mockk<TaskRepository>()
    private val weatherRepository = mockk<WeatherRepository>()

    @Before
    fun setUp() {

        coEvery { repository.getAllTasks() } returns flowOf(emptyList())
        subjectUnderTest = TodoViewModel(
            repository, weatherRepository
        )
    }

    @Test
    fun `getFilteredTasks with  tasks returns only completed Tasks`() = runTest {
        val completedTask1 = Task(
            id = 1,
            title = "Completed Task 1",
            description = "Description 1",
            isCompleted = true
        )
        val completedTask2 = Task(
            id = 2,
            title = "Completed Task 2",
            description = "Description 2",
            isCompleted = true
        )
        val incompleteTask = Task(
            id = 3,
            title = "Incomplete Task",
            description = "Description 3",
            isCompleted = false
        )
        val tasks = listOf(completedTask1, completedTask2, incompleteTask)

        val completedTasksResult = subjectUnderTest.getFilteredTasks(tasks, true)

        assertEquals(2, completedTasksResult.size)
        assertEquals(true, completedTasksResult.all { it.isCompleted })
    }

    @Test
    fun `getFilteredTasks with tasks returns only incomplete tasks`() = runTest {
        val completedTask = Task(
            id = 1,
            title = "Completed Task ",
            description = "Description 1",
            isCompleted = true
        )
        val incompleteTask1 = Task(
            id = 2,
            title = "Incomplete Task 1",
            description = "Description 2",
            isCompleted = false
        )
        val incompleteTask2 = Task(
            id = 3,
            title = "Incomplete Task 2",
            description = "Description 3",
            isCompleted = false
        )
        val incompleteTask3 = Task(
            id = 3,
            title = "Incomplete Task 2",
            description = "Description 3",
            isCompleted = false
        )
        val tasks = listOf(completedTask, incompleteTask1, incompleteTask2, incompleteTask3)


        val incompleteTasksResult = subjectUnderTest.getFilteredTasks(tasks, false)

        assertEquals(3, incompleteTasksResult.size)
        assertEquals(true, incompleteTasksResult.all { it.isCompleted == false })
    }

    @Test
    fun `handleEvent should call deleteTask when DeleteTask event is received`() = runTest {

        val taskId = 1
        coEvery { repository.deleteTask(taskId) } returns Unit

        subjectUnderTest.handleEvent(UiEvents.DeleteTask(taskId))

        advanceUntilIdle()

        coVerify { repository.deleteTask(taskId) }
    }

    @Test
    fun `handleEvent should call addTask when AddTask event is received`() = runTest {

        val task =
            Task(id = 1, title = "Test Task", description = "Test Description", isCompleted = false)
        coEvery { repository.addTask(task) } returns Unit

        subjectUnderTest.handleEvent(UiEvents.AddTask(task))
        advanceUntilIdle()


        coVerify { repository.addTask(task) }
    }

    @Test
    fun `handleEvent should call updateTask when UpdateTask event is received`() = runTest {
        val task =
            Task(id = 1, title = "Test Task", description = "Test Description", isCompleted = true)
        coEvery { repository.updateTask(task) } returns Unit

        subjectUnderTest.handleEvent(UiEvents.UpdateTask(task))

        advanceUntilIdle()

        coVerify { repository.updateTask(task) }
    }
}

