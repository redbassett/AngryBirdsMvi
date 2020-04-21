package com.redbassett.angrybirdsmvi.ui.list

import androidx.lifecycle.liveData
import com.redbassett.angrybirdsmvi.data.BirdRepository
import com.redbassett.angrybirdsmvi.ui.base.BasePresenter
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ListPresenter @Inject constructor(private val repository: BirdRepository)
    : BasePresenter<ListActivity>() {

    private val state = liveData(Dispatchers.IO) {
        emit(Loading)

        try {
            emit(Content(repository.getAllBirds()))
        } catch (e: Exception) {
            emit (Error(e))
        }
    }

    override fun bindView(view: ListActivity) {
        view.apply {
            bindState(state)
        }
    }
}
