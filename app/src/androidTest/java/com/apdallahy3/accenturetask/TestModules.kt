package com.apdallahy3.accenturetask

import androidx.room.Room
import com.apdallahy3.accenturetask.data.source.local.WeatherDatabse
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val roomTestModule = module {
//    single {
//        Room.inMemoryDatabaseBuilder(
//            androidContext(),
//            WeatherDatabse::class.java
//        ).allowMainThreadQueries().build()
//    }

}