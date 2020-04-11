package com.redbassett.angrybirdsmvi.ui.list

import androidx.lifecycle.MutableLiveData
import com.redbassett.angrybirdsmvi.ui.base.BasePresenter
import kotlinx.coroutines.*
import javax.inject.Inject

class ListPresenter @Inject constructor() : BasePresenter<MainActivity>() {

    private val state = MutableLiveData<ListState>(Loading)

    override fun bindView(view: MainActivity) {
        view.apply {
            bindState(state)
        }
    }

    init {
        GlobalScope.launch {
            delay(5000L)
            withContext(Dispatchers.Main) {
                state.value = Error(Throwable("Some error"))
            }
        }
    }
}
