package com.gambia.android.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.apdallahy3.accenturetask.R
import com.apdallahy3.accenturetask.data.source.remote.Resource
import com.apdallahy3.accenturetask.data.source.remote.Status
import com.apdallahy3.accenturetask.databinding.StateErrorListBinding
import com.apdallahy3.accenturetask.base.ErrorMessageHandler as ErrorMessageHandler1


abstract class BaseRVAdapter<T, BINDING : ViewDataBinding>(
    val ctx: Context?,
    var resource: Resource<List<T>?>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    open var errorMessage = "Failed to load date"

    protected abstract fun createDataViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    protected abstract fun bindDataViewHolder(binding: BINDING?, item: T?, position: Int)

    protected abstract fun onRetry()

    open fun createLoadingViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return LoadingItem(
            LayoutInflater.from(parent.context).inflate(
                R.layout.state_loading_list,
                parent,
                false
            )
        )
    }

    open fun createErrorViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ErrorItem(
            LayoutInflater.from(parent.context).inflate(
                R.layout.state_error_list,
                parent,
                false
            )
        )
    }

    open fun createEmptyViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return EmptyItem(
            LayoutInflater.from(parent.context).inflate(
                R.layout.state_empty_list,
                parent,
                false
            )
        )
    }


    fun submitData(data: Resource<List<T>?>?) {
        data?.let {
            resource = it
            notifyDataSetChanged()
            if (data.status == Status.ERROR) {
                Toast.makeText(ctx, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            TYPE_DATA -> createDataViewHolder(parent)
            TYPE_LOADING -> createLoadingViewHolder(parent)
            TYPE_ERROR -> createErrorViewHolder(parent)
            TYPE_EMPTY -> createEmptyViewHolder(parent)
            else -> error("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is BaseRVAdapter<*, *>.ErrorItem -> {
                (holder as BaseRVAdapter<*, *>.ErrorItem).bind(resource)
            }
            is BaseRVAdapter<*, *>.LoadingItem -> {
            }
            is BaseRVAdapter<*, *>.EmptyItem -> {
            }
            else -> bindDataViewHolder(
                DataBindingUtil.bind<BINDING>(holder.itemView),
                resource?.data?.get(position),
                position
            )
        }
    }


    override fun getItemCount(): Int {
        if (resource?.data.isNullOrEmpty()) {
            return when (resource?.status ?: Status.EMPTY) {
                Status.LOADING,
                Status.ERROR,
                Status.SUCCESS,
                Status.EMPTY -> 1
            }
        }

        return resource?.data?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        if (resource?.data.isNullOrEmpty()) {
            return when (resource?.status ?: Status.EMPTY) {
                Status.LOADING -> TYPE_LOADING
                Status.ERROR -> TYPE_ERROR
                Status.SUCCESS -> TYPE_EMPTY
                Status.EMPTY -> TYPE_EMPTY
            }
        }

        return TYPE_DATA
    }

    companion object {
        const val TYPE_DATA = 0
        const val TYPE_ERROR = 1
        const val TYPE_LOADING = 2
        const val TYPE_EMPTY = 3
    }

    inner class LoadingItem(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    inner class ErrorItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(resource: Resource<*>?) {

            val binding = DataBindingUtil.bind<StateErrorListBinding>(itemView)

            binding?.handler = object : ErrorMessageHandler1 {

                override fun getMessage() = resource?.message

                override fun onRetry() {
                    this@BaseRVAdapter.onRetry()
                }
            }

        }
    }


    inner class EmptyItem(itemView: View) : RecyclerView.ViewHolder(itemView)
}
