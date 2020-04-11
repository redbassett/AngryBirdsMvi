package com.redbassett.angrybirdsmvi.ui.base

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

abstract class BaseActivity<T : BaseState> : AppCompatActivity() {
    fun bindState(state: LiveData<T>) {
        state.observe(this, Observer {
            render(it)
        })
    }

    abstract fun render(state: T)
}
