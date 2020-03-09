package com.apdallahy3.accenturetask.data.source.remote

import androidx.lifecycle.LiveData
import com.apdallahy3.accenturetask.data.response.WeatherResponse
import retrofit2.http.*


interface ApiService {


    @GET("weather")
    fun getWeatherInfo(
        @Query("appid") appid: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): LiveData<ApiResponse<WeatherResponse>>

}