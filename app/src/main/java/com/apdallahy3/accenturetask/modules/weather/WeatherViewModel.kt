package com.apdallahy3.accenturetask.modules.weather

import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.apdallahy3.accenturetask.data.reposiories.WeatherRepository
import com.apdallahy3.accenturetask.data.source.local.entities.WeatherModel
import com.gambia.android.base.BaseViewModel
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class WeatherViewModel(val repository: WeatherRepository) : BaseViewModel() {
    private lateinit var locationCallback: LocationCallback
    private val lastLocation = MutableLiveData<Location>()
    val locationNotEnabled = MutableLiveData<Boolean>()
    lateinit var fusedLocation: FusedLocationProviderClient
    lateinit var locationManager: LocationManager
    private var viewModelJob = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + viewModelJob)

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
    //get Weather Form Db
    fun getWeatherData(): LiveData<List<WeatherModel>> {
        return repository.getLatestWeatherInfo()
    }

    //remove Weather from DB
    fun removeWeatherModel(model: WeatherModel) {
        ioScope.launch {
            repository.removeModel(model)
        }
    }


    //Location updates
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

    fun fetchLocation() {
        fusedLocation.lastLocation
            .addOnSuccessListener { location: Location? ->

                location?.let {
                    fetchWeatherInfo(it)
                }

            }
    }
    //Fetch Weather from API
    fun fetchWeatherInfo(location: Location) {
        lastLocation.postValue(location)
    }
    //use transformations to get data from api
    val weatherResource = Transformations.switchMap(lastLocation) {
        repository.getWeeatherData(it)
    }

    fun getCelsiusFromKelvin(fahrenheit: Double?): Double {
        fahrenheit?.let {
            return it - 273.15
        }
        return 0.0

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}