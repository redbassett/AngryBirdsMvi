package com.redbassett.angrybirdsmvi.ui.base

import androidx.lifecycle.ViewModel

abstract class BasePresenter<T : BaseActivity<out BaseState>> : ViewModel() {
    abstract fun bindView(view: T)
}
