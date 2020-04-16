package com.redbassett.angrybirdsmvi.ui.list

import androidx.lifecycle.liveData
import com.redbassett.angrybirdsmvi.data.BirdRepository
import com.redbassett.angrybirdsmvi.ui.base.BasePresenter
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ListPresenter @Inject constructor(val repository: BirdRepository)
    : BasePresenter<ListActivity>() {

    private val state = liveData(Dispatchers.IO) {
        emit(Loading)

        emit(Content(repository.getAllBirds()))
    }

    override fun bindView(view: ListActivity) {
        view.apply {
            bindState(state)
        }
    }

//    init {
//        GlobalScope.launch {
//            delay(5000L)
//            withContext(Dispatchers.Main) {
//                state.value = Error(Throwable("Some error"))
//            }
//        }
//    }
}
