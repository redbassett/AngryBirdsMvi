package com.redbassett.angrybirdsmvi.di

import com.redbassett.angrybirdsmvi.ui.list.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [MviModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
}
