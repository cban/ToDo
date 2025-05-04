package com.application.todo.data.repository

import com.application.todo.data.datasource.TaskDataSource
import com.application.todo.data.model.Task
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class TaskRepositoryImplTest {
    lateinit var subjectUnderTest: TaskRepositoryImpl
    private val localDataSource = mockk<TaskDataSource>()
    lateinit var task: Task

    @Before
    fun setUp() {
        subjectUnderTest = TaskRepositoryImpl(localDataSource)

    }

    @Test
    fun `getAllTasks should return empty flow when localDataSource throws exception`() = runTest {

        val exception = Exception("Test exception")

        coEvery { localDataSource.getAllTasks() } throws exception
        val result = subjectUnderTest.getAllTasks()

        assert(result.toList().isEmpty())
    }

    @Test
    fun `addTask should call insertTask on data source`() = runTest {
        val task =
            Task(id = 1, title = "Test Task", description = "Test Description", isCompleted = false)

        coEvery { localDataSource.insertTask(task) } returns Unit
        subjectUnderTest.addTask(task)

        coVerify(exactly = 1) { localDataSource.insertTask(task) }
    }

    @Test
    fun `updateTask should call updateTask on data source`() = runTest {
        val task = Task(
            id = 1,
            title = "Updated Task",
            description = "Test Description",
            isCompleted = true
        )
        coEvery { localDataSource.updateTask(task) } returns Unit
        subjectUnderTest.updateTask(task)

        coVerify(exactly = 1) { localDataSource.updateTask(task) }
    }

    @Test
    fun `deleteTask should call deleteTask on data source`() = runTest {
        val taskId = 1

        coEvery { localDataSource.deleteTask(taskId) } returns Unit
        subjectUnderTest.deleteTask(taskId)

        coVerify(exactly = 1) { localDataSource.deleteTask(taskId) }
    }
}
