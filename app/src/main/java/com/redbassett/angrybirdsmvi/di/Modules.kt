package com.redbassett.angrybirdsmvi.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AndroidModule(private val application: Application) {
    @Provides
    fun provideContext(): Context = application.applicationContext
}
