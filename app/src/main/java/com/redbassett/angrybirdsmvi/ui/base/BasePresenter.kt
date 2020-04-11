package com.redbassett.angrybirdsmvi.ui.base

abstract class BasePresenter<T : BaseActivity<out BaseState>> {
    abstract fun bindView(view: T)
}
