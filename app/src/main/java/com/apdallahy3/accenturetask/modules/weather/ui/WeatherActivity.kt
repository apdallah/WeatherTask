package com.apdallahy3.accenturetask.modules.weather.ui

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.apdallahy3.accenturetask.R
import com.apdallahy3.accenturetask.base.BaseActionBarActivity
import com.apdallahy3.accenturetask.utils.Constants.Companion.VIEWTYPE_KEY
import com.apdallahy3.accenturetask.utils.SaveClickListner
import org.koin.android.ext.android.inject

class WeatherActivity : BaseActionBarActivity<ViewDataBinding>() {
    private lateinit var saveClick: SaveClickListner
     override fun updateUI(savedInstanceState: Bundle?) {

        setScreenTitle(getString(R.string.app_name))

        headerDataBinding.save.setOnClickListener {
            saveClick.onSaveClick()

        }
        val fragment = WeatherFragment.newInstance()
        val args=Bundle()
        if(headerDataBinding.details != null){
            args.putBoolean(VIEWTYPE_KEY,true)
        }else{
            args.putBoolean(VIEWTYPE_KEY,false)

        }
        fragment.arguments=args
        supportFragmentManager.beginTransaction()
            .add(R.id.container, fragment, "WeatherInfo").addToBackStack("weather").commit()

    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is WeatherFragment) {
            headerDataBinding.save.visibility = View.VISIBLE
            saveClick = fragment as WeatherFragment
        }
    }

}
