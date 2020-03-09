package com.apdallahy3.accenturetask

import android.app.Application
import androidx.multidex.MultiDex
import com.apdallahy3.accenturetask.koin.appModule
import com.apdallahy3.accenturetask.koin.repo
import com.apdallahy3.accenturetask.koin.viewModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin


class App : Application() {

    val x = "x string"


    override fun onCreate() {
        super.onCreate()
        initKoin()
        MultiDex.install(this)

        appInstance = this
    }

    fun initKoin() {
        stopKoin()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(appModule, viewModule, repo))
        }
    }

    companion object {
        lateinit var appInstance: App

    }


}