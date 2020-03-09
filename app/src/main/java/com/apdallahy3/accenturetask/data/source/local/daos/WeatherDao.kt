package com.apdallahy3.accenturetask.data.source.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.apdallahy3.accenturetask.data.source.local.entities.WeatherModel

@Dao
interface WeatherDao {

    @Query("select * from weather_table")
    fun getWeatherList(): List<WeatherModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWeather(weatherModel: WeatherModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWeather(weatherModel: List<WeatherModel>)

    @Query("select * from weather_table order by date desc ")
    fun getLatestWeather(): LiveData<List<WeatherModel>>

    @Update
    fun updateWeather(item: WeatherModel)

    @Delete
    suspend fun remove(item: WeatherModel)


}