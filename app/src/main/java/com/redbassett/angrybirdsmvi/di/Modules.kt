package com.redbassett.angrybirdsmvi.di

import com.redbassett.angrybirdsmvi.ui.list.ListPresenter
import dagger.Module
import dagger.Provides

//@Module
//class ContextModule(@get:Provides val context: Context)

@Module
class MviModule {
    @Provides
    fun provideListPresenter() = ListPresenter()
}
