package com.redbassett.angrybirdsmvi.di

import android.app.Application
import com.redbassett.angrybirdsmvi.data.BirdRepository
import com.redbassett.angrybirdsmvi.ui.list.ListPresenter
import dagger.Module
import dagger.Provides

//@Module
//class ContextModule(@get:Provides val context: Context)

@Module
class AndroidModule(val application: Application) {
    @Provides
    fun provideContext() = application.applicationContext
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
