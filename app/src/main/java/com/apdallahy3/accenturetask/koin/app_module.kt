package com.apdallahy3.accenturetask.koin

import androidx.room.Room
import com.apdallahy3.accenturetask.data.source.local.WeatherDatabse
import com.apdallahy3.accenturetask.data.source.remote.ApiServiceFactory
import com.apdallahy3.accenturetask.data.source.remote.ContextProviders
 import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module


val appModule: Module = module {


    single { ApiServiceFactory.getService() }
    single { ContextProviders.getInstance() }
    single { Room.databaseBuilder(
            androidContext(),
            WeatherDatabse::class.java, "weather-database"
        ).fallbackToDestructiveMigration().build()
    }

}


