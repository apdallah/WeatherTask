package com.apdallahy3.accenturetask.data.source.local.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.NotNull
import java.util.*

@Parcelize
@Entity(tableName = "weather_table")
data class WeatherModel(

    @PrimaryKey
    @ColumnInfo(name = "id")
    @NotNull
    var id: Int,
    var name: String? = null,
    var lat: Double? = null,
    var lon: Double? = null,
    var description: String? = null,
    var icon: String? = null,
    var date: Date? = null,
    var temp: Double? = null,
    val pressure: Int? = null,
    val humidity: Int? = null


) : Parcelable