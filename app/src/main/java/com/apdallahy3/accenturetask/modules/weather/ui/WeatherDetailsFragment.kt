package com.apdallahy3.accenturetask.modules.weather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.apdallahy3.accenturetask.R
import com.apdallahy3.accenturetask.data.source.local.entities.WeatherModel

import com.apdallahy3.accenturetask.databinding.FragmentWeatherDetailsBinding

class WeatherDetailsFragment : Fragment() {

    lateinit var dataBinding: FragmentWeatherDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_weather_details, container, false)
        arguments?.let {
            dataBinding.weather = it.getParcelable<WeatherModel>("weather")
        }
        return dataBinding.root

    }


    companion object {
        @JvmStatic
        fun newInstance() =
            WeatherDetailsFragment()
    }

}
