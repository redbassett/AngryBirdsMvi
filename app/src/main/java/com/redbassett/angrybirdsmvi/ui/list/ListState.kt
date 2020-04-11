package com.redbassett.angrybirdsmvi.ui.list

import com.redbassett.angrybirdsmvi.data.model.Bird
import com.redbassett.angrybirdsmvi.ui.base.BaseState


sealed class ListState : BaseState

data class Error(val error: Throwable) : ListState()
data class Content(val birds: List<Bird>) : ListState()
object Loading : ListState()
