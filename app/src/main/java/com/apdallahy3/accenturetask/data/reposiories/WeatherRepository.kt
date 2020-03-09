package com.apdallahy3.accenturetask.data.reposiories

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import com.apdallahy3.accenturetask.data.response.WeatherResponse
import com.apdallahy3.accenturetask.data.source.local.WeatherDatabse
import com.apdallahy3.accenturetask.data.source.local.entities.WeatherModel
import com.apdallahy3.accenturetask.data.source.remote.*
import java.util.*


class WeatherRepository(
    private val apiService: ApiService, private val coroutineContext: ContextProviders
    , private val database: WeatherDatabse
) {
    fun getWeeatherData(location: Location): LiveData<Resource<WeatherModel>> {
        return object :
            NetworkBoundResource<WeatherModel, WeatherResponse>(coroutineContext) {
            private var result: WeatherModel? = null
            override fun saveCallResult(item: WeatherResponse) {
                val model = getWeatherModelFromReponse(location, item)
                saveWeatherInfo(model)
            }

            override fun getResult(): WeatherModel? = result

            override fun createCall(): LiveData<ApiResponse<WeatherResponse>> {
                return apiService.getWeatherInfo(
                    lat = location.latitude,
                    lon = location.longitude,
                    appid = ClientConstants.API_ID
                )
            }

            override fun shouldFetch(data: WeatherModel?): Boolean = true

            override fun loadFromDb(): LiveData<WeatherModel>? = null

        }.asLiveData()
    }

    private fun getWeatherModelFromReponse(
        location: Location,
        response: WeatherResponse
    ): WeatherModel {
        Log.i("getWeatherModelF", response.main?.temp.toString())
        return WeatherModel(
            name = response.name,
            lon = location.longitude,
            lat = location.latitude,
            date = Calendar.getInstance().time,
            description = response.weather?.get(0)?.description,
            icon = response.weather?.get(0)?.icon,
            temp = response.main!!.temp,
            humidity = response.main.humidity,
            pressure = response.main.pressure

        )
    }

    private fun saveWeatherInfo(weatherModel: WeatherModel) {
        database.getWeatherDao().insertWeather(weatherModel)
    }
   suspend fun removeModel(model: WeatherModel){

        database.getWeatherDao().remove(model)
    }

    fun getLatestWeatherInfo(): LiveData<List<WeatherModel>> {
        return database.getWeatherDao().getLatestWeather()
    }


}