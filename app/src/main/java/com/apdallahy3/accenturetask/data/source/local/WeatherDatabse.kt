package com.apdallahy3.accenturetask.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.apdallahy3.accenturetask.data.source.local.daos.WeatherDao
import com.apdallahy3.accenturetask.data.source.local.entities.WeatherModel

@TypeConverters(TimestampConverter::class)

@Database(
    entities = [WeatherModel::class],
    version = 1,
    exportSchema = false
)
abstract class WeatherDatabse : RoomDatabase() {
     abstract fun getWeatherDao(): WeatherDao
}