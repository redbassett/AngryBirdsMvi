package com.redbassett.angrybirdsmvi.ui.list

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.redbassett.angrybirdsmvi.AngryBirdsApp
import com.redbassett.angrybirdsmvi.R
import com.redbassett.angrybirdsmvi.data.model.Bird
import com.redbassett.angrybirdsmvi.ui.base.BaseActivity
import com.redbassett.angrybirdsmvi.util.quickToast
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@Suppress("EXPERIMENTAL_API_USAGE")
class ListActivity : BaseActivity<ListState>() {

    @Inject lateinit var presenter: ListPresenter

    private lateinit var viewAdapter: BirdListViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AngryBirdsApp).appComponent.inject(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_list)

        presenter.bindView(this)

        viewManager = LinearLayoutManager(this)
        viewAdapter = BirdListViewAdapter()

        bird_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        presenter.loadMoreFlow.postValue(viewAdapter.loadMoreFlow)
    }

    override fun render(state: ListState) {
        when (state) {
            is Loading -> {
                loading_bar.visibility = View.VISIBLE
                error_group.visibility = View.GONE
                bird_list.visibility = View.GONE
            }
            is Error -> {
                loading_bar.visibility = View.GONE
                error_group.visibility = View.VISIBLE
                bird_list.visibility = View.GONE
                error_message.text = state.error.localizedMessage
                quickToast(state.error.localizedMessage ?: "Unknown error")
            }
            is Content -> {
                loading_bar.visibility = View.GONE
                error_group.visibility = View.GONE
                bird_list.visibility = View.VISIBLE
                viewAdapter.setData(state.birds, state.lastPageReached)
                viewAdapter.notifyDataSetChanged()
            }
        }
    }
}

@ExperimentalCoroutinesApi
class BirdListViewAdapter : ContinuousScrollAdapter<Bird>(
    BirdContentViewHolder::class,
    R.layout.bird_list_item,
    LoadingViewHolder::class,
    R.layout.load_more_list_item
) {

    override fun onBindContentViewHolder(holder: LoadMoreViewHolder.ContentHolder, position: Int) {
        val bird = data[position]
        (holder as BirdContentViewHolder).apply {
            itemView.apply {
                this.findViewById<TextView>(R.id.bird_name).text = bird.name
                this.findViewById<TextView>(R.id.bird_description).text = bird.description

                Glide
                    .with(this)
                    .load("https://angry-birds-api.herokuapp.com/img/thumbs/${bird.image}.jpg")
                    .centerCrop()
                    .into(itemView.findViewById(R.id.bird_image))
            }
        }
    }

    override fun onBindLoadingViewHolder(
        holder: LoadMoreViewHolder.LoadingHolder,
        position: Int,
        lastPageReached: Boolean
    ) {
        (holder as LoadingViewHolder).apply {
            itemView.apply {
                findViewById<ProgressBar>(R.id.loading_more_bar).visibility =
                    if (lastPageReached) View.GONE else View.VISIBLE
                findViewById<TextView>(R.id.loading_more_end_of_list_text).visibility =
                    if (lastPageReached) View.VISIBLE else View.GONE
            }
        }
    }

    class BirdContentViewHolder(itemView: View) : LoadMoreViewHolder.ContentHolder(itemView)
    class LoadingViewHolder(itemView: View) : LoadMoreViewHolder.LoadingHolder(itemView)
}
