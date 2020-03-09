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
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import com.apdallahy3.accenturetask.R
import com.apdallahy3.accenturetask.data.source.remote.Status
import com.apdallahy3.accenturetask.databinding.FragmentWeatherBinding
import com.apdallahy3.accenturetask.modules.weather.WeatherViewModel
import com.apdallahy3.accenturetask.utils.Constants
import com.apdallahy3.accenturetask.utils.SaveClickListner
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherFragment : Fragment(), SaveClickListner {

    lateinit var dataBinding: FragmentWeatherBinding
    private val REQUEST_LOCATION_PERMISION = 500
    private val REQUEST_CHECK_SETTINGS = 600
    private val viewModel: WeatherViewModel by viewModel()
    private var view_type: Boolean = false

    @SuppressLint("NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_weather, container, false)
        viewModel.locationManager = activity!!.getSystemService(LOCATION_SERVICE) as LocationManager
        viewModel.fusedLocation = LocationServices.getFusedLocationProviderClient(context!!)
        arguments?.let {
            view_type = it.getBoolean(Constants.VIEWTYPE_KEY)
        }
        observeWeatherInfo()
        dataBinding.moreDetails.setOnClickListener {
            navigateToDetails()
        }
        viewModel.weatherModel.observe(viewLifecycleOwner, Observer { weather ->
            weather?.let {
                it.temp = viewModel.getCelsiusFromKelvin(it.temp)
                dataBinding.weather = it
                dataBinding.executePendingBindings()
            }

        })
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                viewModel.fetchLocation()

            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }

        } else {
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                REQUEST_LOCATION_PERMISION
            )
        }
        return dataBinding.root

    }

    fun navigateToDetails() {
        val fragment = WeatherDetailsFragment.newInstance()
        val args = Bundle()
        args.putParcelable("weather", viewModel.getWeatherModel())
        fragment.arguments = args
        if (view_type)
            activity!!.supportFragmentManager.beginTransaction().replace(
                R.id.details,
                fragment
            ).commit()
        else
            activity!!.supportFragmentManager.beginTransaction().add(
                R.id.container,
                fragment
            ).addToBackStack("details").commit()

    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun observeWeatherInfo() {
        viewModel.weatherResource.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    showLoading()
                }
                Status.SUCCESS -> {
                    hideLoading()
                    viewModel.setWeatherModel(model = it.data)
                }
                Status.ERROR -> {
                    hideLoading()
                }
            }
        })
    }

    private fun showLoading() {
        dataBinding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        dataBinding.progressBar.visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopLocationUpdates()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.i("onRequestResult", requestCode.toString())
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            viewModel.setLocationUpdateListener()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            WeatherFragment()
    }


    override fun onSaveClick() {
        viewModel.fetchLocation()
    }
}
