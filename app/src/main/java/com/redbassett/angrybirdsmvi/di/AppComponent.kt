package com.redbassett.angrybirdsmvi.di

import com.redbassett.angrybirdsmvi.ui.list.ListActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidModule::class])
interface AppComponent {
    fun inject(activity: ListActivity)
}
