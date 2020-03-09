package com.apdallahy3.accenturetask.koin

import com.apdallahy3.accenturetask.modules.weather.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

//define list view model
val viewModule = module {
    viewModel { WeatherViewModel(get()) }
}