package com.redbassett.angrybirdsmvi.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.reflect.KClass

@Suppress("NO_REFLECTION_IN_CLASS_PATH")
@ExperimentalCoroutinesApi
abstract class ContinuousScrollAdapter<T>(
    contentClazz: KClass<out LoadMoreViewHolder.ContentHolder>,
    @LayoutRes private val contentLayout: Int,
    loadingClazz: KClass<out LoadMoreViewHolder.LoadingHolder>,
    @LayoutRes private val loadingLayout: Int
) : RecyclerView.Adapter<LoadMoreViewHolder>() {

    protected var data: List<T> = arrayListOf()
    private var lastPageReached = false

    private var notified = false

    fun setData(data: List<T>, lastPageReached: Boolean) {
        this.data = data
        this.lastPageReached = lastPageReached
        this.notified = false
    }

    private val contentConstructor = try {
        contentClazz.constructors.first {
            it.parameters.size == 1 && it.parameters.filter { param ->
                param.name == LoadMoreViewHolder::class.constructors.first().parameters.first().name
                        && param.type == LoadMoreViewHolder::class.constructors.first().parameters.first().type
            }.size == 1
        }
    } catch (e: NoSuchElementException) {
        throw Exception("Content ViewHolder must have a single-parameter constructor")
    }

    private val loadingConstructor = try {
        loadingClazz.constructors.first {
            it.parameters.size == 1 && it.parameters.filter { param ->
                param.name == LoadMoreViewHolder::class.constructors.first().parameters.first().name
                        && param.type == LoadMoreViewHolder::class.constructors.first().parameters.first().type
            }.size == 1
        }
    } catch (e: NoSuchElementException) {
        throw Exception("Loading ViewHolder must have a single-parameter constructor")
    }


    // Flow exposed to consumers allows them to bind to a channel of Unit where each emitted Unit
    // represents an event caused by the last item being reached
    val loadMoreFlow: Flow<Unit> = callbackFlow {
        lastItemListener = fun() {
            safeOffer(Unit)
        }
        awaitClose { lastItemListener = null }
    }
    private var lastItemListener: (() -> Unit)? = null

    // Extension function to add exception handling to flow channel offer method
    private fun <E> SendChannel<E>.safeOffer(value: E) = !isClosedForSend && try {
        offer(value)
    } catch (e: CancellationException) {
        false
    }


    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadMoreViewHolder {
        fun inflate(@LayoutRes layoutId: Int) =
            LayoutInflater.from(parent.context).inflate(layoutId, parent, false)

        return when (viewType) {
            0 -> contentConstructor.call(inflate(contentLayout))
            1 -> loadingConstructor.call(inflate(loadingLayout))
            else -> throw Exception("Unexpected viewType: $viewType")
        }
    }

    final override fun onBindViewHolder(holder: LoadMoreViewHolder, position: Int) {
        when (holder) {
            is LoadMoreViewHolder.ContentHolder -> onBindContentViewHolder(holder, position)
            is LoadMoreViewHolder.LoadingHolder -> {
                if (!notified && !lastPageReached) {
                    lastItemListener?.invoke()
                    notified = true
                }
                onBindLoadingViewHolder(holder, position, lastPageReached)
            }
        }
    }

    final override fun getItemCount() = data.count() + 1

    final override fun getItemViewType(position: Int) = if (position < data.count()) 0 else 1


    // Pseudo-onBindViewHolder() methods for each type of ViewHolder
    abstract fun onBindContentViewHolder(holder: LoadMoreViewHolder.ContentHolder, position: Int)
    abstract fun onBindLoadingViewHolder(holder: LoadMoreViewHolder.LoadingHolder,
                                         position: Int,
                                         lastPageReached: Boolean = false)
}

sealed class LoadMoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract class ContentHolder(itemView: View) : LoadMoreViewHolder(itemView)
    abstract class LoadingHolder(itemView: View) : LoadMoreViewHolder(itemView)
}
