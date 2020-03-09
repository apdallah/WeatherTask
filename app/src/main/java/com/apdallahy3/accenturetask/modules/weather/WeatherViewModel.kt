package com.apdallahy3.accenturetask.modules.weather

import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.apdallahy3.accenturetask.data.reposiories.WeatherRepository
import com.apdallahy3.accenturetask.data.source.local.entities.WeatherModel
import com.apdallahy3.accenturetask.koin.viewModule
import com.gambia.android.base.BaseViewModel
import com.google.android.gms.location.*


class WeatherViewModel(val repository: WeatherRepository) : BaseViewModel() {
    private lateinit var locationCallback: LocationCallback
    private val lastLocation = MutableLiveData<Location>()
    val locationNotEnabled = MutableLiveData<Boolean>()
    lateinit var fusedLocation: FusedLocationProviderClient
    lateinit var locationManager: LocationManager
    var weatherModel = MutableLiveData<WeatherModel>()
    val weatherResource = Transformations.switchMap(lastLocation) {
        repository.getWeeatherData(it)
    }

    val locationSettingRequest = LocationSettingsRequest.Builder()
        .addLocationRequest(buildLocationRequest())

    init {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {

                if (locationResult == null) {
                    locationNotEnabled.postValue(true)
                } else {
                    fetchLocation()

                }

            }

        }
    }


    fun stopLocationUpdates() {
        fusedLocation.removeLocationUpdates(locationCallback)
    }

    fun setLocationUpdateListener() {
        fusedLocation.requestLocationUpdates(
            buildLocationRequest(),
            locationCallback,
            Looper.getMainLooper()
        )

    }

    private fun buildLocationRequest(): LocationRequest {

        return LocationRequest()
            .setNumUpdates(1)
            .setExpirationDuration(60000)
            .setInterval(1)
            .setFastestInterval(1)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

    }

    fun getWeatherModel(): WeatherModel? = weatherModel.value
    fun setWeatherModel(model: WeatherModel?) {
        weatherModel.postValue(model)
    }

    fun fetchLocation() {
        fusedLocation.lastLocation
            .addOnSuccessListener { location: Location? ->

                location?.let {
                    getWeatherInfo(it)
                }

            }
    }

    fun getWeatherInfo(location: Location) {
        lastLocation.postValue(location)
    }

    fun getCelsiusFromKelvin(fahrenheit: Double?): Double {
        fahrenheit?.let {
            return it - 273.15
        }
        return 0.0

    }
}