package com.application.todo.data.datasource.network

import com.application.todo.data.model.AstronomyDto
import com.application.todo.data.model.WeatherResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class WeatherDataSourceTest {
    private var api: WeatherApi = mockk<WeatherApi>()
    private lateinit var subjectUnderTest: WeatherDataSource


    @Before
    fun setUp() {
        subjectUnderTest = WeatherDataSourceImpl(api)
    }

    @Test
    fun `getCurrentWeather returns WeatherResponse when API call is successful`() = runTest {

        val location = "-29.097469,28.8790"
        val mockResponse = mockk<WeatherResponse>()

        coEvery { api.getCurrentWeather(location) } returns Response.success(mockResponse)

        val result = subjectUnderTest.getCurrentWeather(location)

        assertEquals(mockResponse, result)
        coVerify { api.getCurrentWeather(location) }

    }

    @Test
    fun `getCurrentWeather throws exception with correct message when API call fails`() = runTest {

        val location = "-29.097469,28.8790"
        coEvery { api.getCurrentWeather(location) } returns mockk {
            every { isSuccessful } returns false
        }

        val exception = assertThrows(Exception::class.java) {
            runBlocking { subjectUnderTest.getCurrentWeather(location) }
        }
        assertEquals("Error fetching weather data", exception.message)
        coVerify { api.getCurrentWeather(location) }
    }

    @Test
    fun `getWeatherAstronomy returns AstronomyDto when API call is successful`() = runTest {

        val location = "-29.097469,28.8790"
        val mockResponse = mockk<AstronomyDto>()

        coEvery { api.getWeatherAstronomy(location) } returns Response.success(mockResponse)

        val result = subjectUnderTest.getWeatherAstronomy(location)

        assertEquals(mockResponse, result)
        coVerify { api.getWeatherAstronomy(location) }

    }

    @Test
    fun `getWeatherAstronomy throws exception with correct message when API call fails`() =
        runTest {

            val location = "-29.097469,28.8790"
            coEvery { api.getWeatherAstronomy(location) } returns mockk {
                every { isSuccessful } returns false
            }

            val exception = assertThrows(Exception::class.java) {
                runBlocking { subjectUnderTest.getWeatherAstronomy(location) }
            }
            assertEquals("Error fetching weather data", exception.message)
            coVerify { api.getWeatherAstronomy(location) }
        }
}