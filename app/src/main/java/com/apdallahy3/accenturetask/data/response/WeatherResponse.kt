package com.apdallahy3.accenturetask.data.response

class WeatherResponse() {
    val id: Int? = null
    val name: String? = null
    val code: Int? = null
    val weather: List<Weather>? = null
    val main: MainData? = null
    val coord: CoordData? = null
}

data class Weather(
    val id: Int? = null,
    val main: String? = null,
    val description: String? = null,
    val icon: String? = null

)

data class MainData(
    val temp: Double? = null,
    val pressure: Int? = null,
    val humidity: Int? = null
)

data class CoordData(
    val lon: Double? = null,
    val lat: Double? = null
)