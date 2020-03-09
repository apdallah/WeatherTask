package com.apdallahy3.accenturetask.data.source.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.apdallahy3.accenturetask.data.source.local.entities.WeatherModel

@Dao
interface WeatherDao {

    @Query("select * from weather_table")
    fun getWeatherList():List<WeatherModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWeather(weatherModel: WeatherModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWeather(weatherModel: List<WeatherModel>)

    @Query("select * from weather_table where lat=:lat and lon=:lon order by date desc limit 1")
    fun getLatestWeather(lat: Double, lon: Double): LiveData<WeatherModel>

    @Update
    fun updateWeather(item: WeatherModel)


}