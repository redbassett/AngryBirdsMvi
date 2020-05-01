package com.redbassett.angrybirdsmvi.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.redbassett.angrybirdsmvi.data.BirdRepository
import com.redbassett.angrybirdsmvi.data.model.Bird
import com.redbassett.angrybirdsmvi.ui.base.BasePresenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListPresenter @Inject constructor(private val repository: BirdRepository)
    : BasePresenter<ListActivity>() {

    private var currentPage = 1
    private var lastPageReached = false
    private val birdsList = arrayListOf<Bird>()

    private val state = MutableLiveData<ListState>()

    val loadMoreFlow = MutableLiveData<Flow<Unit>>()
    private val loadMoreObserver = Observer<Flow<Unit>> {
        viewModelScope.launch {
            it.collect {
                onLastItemReached()
            }
        }
    }

    init {
        loadBirds(currentPage)
        loadMoreFlow.observeForever(loadMoreObserver)
    }

    override fun onCleared() {
        loadMoreFlow.removeObserver(loadMoreObserver)
        super.onCleared()
    }

    override fun bindView(view: ListActivity) {
        view.apply {
            bindState(state)
        }
    }

    private fun loadBirds(page: Int) {
        state.apply {
            if (birdsList.isEmpty()) value = Loading

            viewModelScope.launch {
                val result = repository.getBirds(page)
                if (result.isEmpty()) {
                    lastPageReached = true
                    postValue(Content(birdsList, true))
                } else {
                    birdsList.addAll(result)
                    // TODO: 4/25/20 Catch errors
                    postValue(Content(birdsList))
                }
            }
        }
    }

    private fun onLastItemReached() {
        currentPage++
        loadBirds(currentPage)
    }
}
