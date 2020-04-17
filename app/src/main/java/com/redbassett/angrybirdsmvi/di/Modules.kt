package com.redbassett.angrybirdsmvi.di

import android.app.Application
import android.content.Context
import com.redbassett.angrybirdsmvi.data.BirdRepository
import com.redbassett.angrybirdsmvi.ui.list.ListPresenter
import dagger.Module
import dagger.Provides

@Module
class AndroidModule(private val application: Application) {
    @Provides
    fun provideContext(): Context = application.applicationContext
}

@Module(includes = [DataModule::class])
class MviModule {
    @Provides
    fun provideListPresenter() = ListPresenter(BirdRepository)
}

@Module
class DataModule {
    @Provides
    fun provideBirdRepository() = BirdRepository
}
