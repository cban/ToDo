package com.application.todo.data.datasource.local

import com.application.todo.data.datasource.local.db.TaskDao
import com.application.todo.data.datasource.local.db.TaskEntity
import com.application.todo.data.mappers.toDomain
import com.application.todo.data.mappers.toEntity
import com.application.todo.data.model.Task
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LocalDataSourceImplTest {

    private var taskDao: TaskDao = mockk<TaskDao>()
    private lateinit var subjectUnderTest: LocalDataSourceImpl

    @Before
    fun setUp() {
        subjectUnderTest = LocalDataSourceImpl(taskDao)
    }

    @Test
    fun `verify LocalDataSourceImpl hits dao for getAllTasks`() = runTest {
        coEvery { taskDao.getAllTasks() } returns mockk()
        subjectUnderTest.getAllTasks()
        coVerify { (taskDao).getAllTasks() }
    }


    @Test
    fun `verify LocalDataSourceImpl hits dao for insertTask`() = runTest {
        val task = mockk<Task>()
        val taskEntity = mockk<TaskEntity>()

        mockkStatic("com.application.todo.data.mappers.MappersKt")
        every { task.toEntity() } returns taskEntity
        coEvery { taskDao.insertTask(any()) } returns Unit

        subjectUnderTest.insertTask(task)

        coVerify { taskDao.insertTask(taskEntity) }
    }

    @Test
    fun `verify LocalDataSourceImpl hits dao for updateTask`() = runTest {
        val taskId = 1
        val task = mockk<Task>()
        val taskEntity = mockk<TaskEntity>()

        mockkStatic("com.application.todo.data.mappers.MappersKt")
        every { task.id } returns taskId
        every { task.toEntity(taskId) } returns taskEntity
        coEvery { taskDao.updateTask(any()) } returns Unit

        subjectUnderTest.updateTask(task)

        coVerify { taskDao.updateTask(taskEntity) }
    }

    @Test
    fun `verify LocalDataSourceImpl hits dao for deleteTask`() = runTest {
        val taskId = 1
        coEvery { taskDao.deleteTask(any()) } returns Unit

        subjectUnderTest.deleteTask(taskId)

        coVerify { taskDao.deleteTask(taskId) }
    }

    @Test
    fun `getAllTasks should return mapped domain task`() = runTest {
        val taskEntity = mockk<TaskEntity>()
        val taskEntities = listOf(taskEntity)
        val expectedTask = mockk<Task>()
        val expectedTasks = listOf(expectedTask)

        every { taskDao.getAllTasks() } returns flowOf(taskEntities)
        every { taskEntity.id } returns 1
        every { taskEntity.isCompleted } returns false
        mockkStatic("com.application.todo.data.mappers.MappersKt")
        every { taskEntity.toDomain() } returns expectedTask

        val result = subjectUnderTest.getAllTasks().first()

        assertEquals(expectedTasks, result)
        coVerify(exactly = 1) { taskDao.getAllTasks() }
    }
}