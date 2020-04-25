package com.redbassett.angrybirdsmvi.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.redbassett.angrybirdsmvi.data.BirdRepository
import com.redbassett.angrybirdsmvi.data.model.Bird
import com.redbassett.angrybirdsmvi.ui.base.BasePresenter
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListPresenter @Inject constructor(private val repository: BirdRepository)
    : BasePresenter<ListActivity>(), BirdListViewAdapter.LastItemNotifier {

    private var currentPage = 1
    private var lastPageReached = false
    private val birdsList = arrayListOf<Bird>()

    private val state = MutableLiveData<ListState>()

    init {
        loadBirds(currentPage)
    }

    override fun bindView(view: ListActivity) {
        view.apply {
            bindState(state)
            notifier = this@ListPresenter
        }
    }

    private fun loadBirds(page: Int) {
        state.apply {
            if (birdsList.isEmpty()) value = Loading

            viewModelScope.launch {
                val result = repository.getBirds(page)
                if (result.isEmpty()) {
                    lastPageReached = true
                    value = Content(birdsList, true)
                } else {
                    birdsList.addAll(result)
                    // TODO: 4/25/20 Catch errors
                    value = Content(birdsList)
                }
            }
        }
    }

    override fun onLastItemReached() {
        loadBirds(currentPage++)
    }
}
