package com.apdallahy3.accenturetask

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.apdallahy3.accenturetask.data.source.local.WeatherDatabse
import com.apdallahy3.accenturetask.data.source.local.daos.WeatherDao
import com.apdallahy3.accenturetask.data.source.local.entities.WeatherModel
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject

@RunWith(AndroidJUnit4::class)
class DatabaseTest : KoinTest {
    private lateinit var weatherDao: WeatherDao
    val weatherDatabse: WeatherDatabse by inject()
    @Before
    fun initDao() {
        //switch between memory db module and local db befor run test
        loadKoinModules(roomTestModule)
        weatherDao = weatherDatabse.getWeatherDao()

    }

    @Test
    fun testSaveAndRetrive() {
        val weathers = listOf(
            WeatherModel(1, "TestTitle", 42.0, 45.0),
            WeatherModel(2, "TestTitle2", 48.0, 50.2)
        )

        // Save entities
        weatherDao.insertWeather(weathers)

        // Request Entities
        val savedPlaces = weatherDao.getWeatherList()
        System.out.println(savedPlaces!!.size.toString())

        // compare result
        Assert.assertEquals(weathers, savedPlaces)
    }

    @After
    fun after() {
        weatherDatabse.close()
        stopKoin()
    }

}