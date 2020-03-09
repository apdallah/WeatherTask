package com.apdallahy3.accenturetask.modules.weather.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.apdallahy3.accenturetask.R
import com.apdallahy3.accenturetask.data.source.local.entities.WeatherModel
import com.apdallahy3.accenturetask.data.source.remote.Resource
import com.apdallahy3.accenturetask.databinding.WeatherItemBinding
import com.bumptech.glide.Glide
import com.gambia.android.base.BaseRVAdapter

class WeatherAdapter(
    val context: Context,
    resource: Resource<List<WeatherModel>>,
     val onclickCallback: ((item: WeatherModel) -> Unit)? = null,
    val removeCallback: ((item: WeatherModel) -> Unit)? = null
) :
    BaseRVAdapter<WeatherModel, WeatherItemBinding>(context, resource) {


    override fun bindDataViewHolder(
        binding: WeatherItemBinding?,
        item: WeatherModel?,
        position: Int
    ) {

        binding?.let { bind ->
            bind.weather = item
            bind.moreDetails.setOnClickListener {
                onclickCallback?.invoke(item!!)
            }
            bind.remove.setOnClickListener {
                removeCallback?.invoke(item!!)
            }
            bind.executePendingBindings()
        }
    }
    fun updateData(){
        resource?.data
    }

    override fun onRetry() {

    }

    override fun createDataViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.weather_item,
                parent,
                false
            )
        )
    }


    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}


}