package com.task.bosta.presentation

import android.app.Application
import com.task.bosta.di.AppComponent
import com.task.bosta.di.DaggerAppComponent

class BostaApplication : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().application(this).build()
    }
}