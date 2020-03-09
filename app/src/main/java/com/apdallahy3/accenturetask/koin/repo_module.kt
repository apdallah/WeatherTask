package com.apdallahy3.accenturetask.koin

import com.apdallahy3.accenturetask.data.reposiories.WeatherRepository
import org.koin.dsl.module


val repo = module {
    single {
        WeatherRepository(
            get(),
            get(),
            get()
        )
    }
}