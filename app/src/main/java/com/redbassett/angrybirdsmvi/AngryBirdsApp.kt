package com.redbassett.angrybirdsmvi

import android.app.Application
import com.redbassett.angrybirdsmvi.di.AndroidModule
import com.redbassett.angrybirdsmvi.di.AppComponent
import com.redbassett.angrybirdsmvi.di.DaggerAppComponent

class AngryBirdsApp : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .androidModule(AndroidModule(this))
            .build()
    }
}
