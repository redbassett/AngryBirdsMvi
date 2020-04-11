package com.redbassett.angrybirdsmvi

import android.app.Application
import com.redbassett.angrybirdsmvi.di.DaggerAppComponent

class AngryBirdsApp : Application() {
    val appComponent = DaggerAppComponent.create()
}
