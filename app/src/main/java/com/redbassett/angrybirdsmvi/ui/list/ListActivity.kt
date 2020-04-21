package com.redbassett.angrybirdsmvi.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import javax.inject.Inject

class ListActivity : BaseActivity<ListState>(), BirdListViewAdapter.LastItemNotifier {

    @Inject lateinit var presenter: ListPresenter

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AngryBirdsApp).appComponent.inject(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_list)

        presenter.bindView(this)

        viewManager = LinearLayoutManager(this)
        viewAdapter = BirdListViewAdapter(this)
        bird_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
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
                quickToast(state.error.localizedMessage)
            }
            is Content -> {
                loading_bar.visibility = View.GONE
                error_group.visibility = View.GONE
                bird_list.visibility = View.VISIBLE
                (viewAdapter as BirdListViewAdapter).apply {
                    birds = state.birds
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun onLastItemReached() = quickToast("Loading more content")
}

class BirdListViewAdapter(val notifier: LastItemNotifier)
    : RecyclerView.Adapter<BirdListViewAdapter.ListViewHolder>() {

    var birds: List<Bird> = arrayListOf()

    sealed class ListViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {

        class LoadingViewHolder(view: View) : ListViewHolder(view) {
            val progressBar: ProgressBar = view.findViewById(R.id.loading_more_bar)
        }

        class BirdViewHolder(view: View) : ListViewHolder(view) {
            val name: TextView = view.findViewById(R.id.bird_name)
            val description: TextView = view.findViewById(R.id.bird_description)
            val image: ImageView = view.findViewById(R.id.bird_image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        fun inflate(isBirdItem: Boolean): View {
            return LayoutInflater.from(parent.context)
                .inflate(if (isBirdItem) R.layout.bird_list_item else R.layout.load_more_list_item,
                parent, false)
        }

        return if (viewType == ITEM_TYPE_BIRD) {
            ListViewHolder.BirdViewHolder(inflate(true))
        } else {
            ListViewHolder.LoadingViewHolder(inflate(false))
        }
    }

    override fun onBindViewHolder(holder: BirdListViewAdapter.ListViewHolder, position: Int) {
        when (holder) {
            is ListViewHolder.BirdViewHolder -> {
                val bird = birds[position]
                holder.apply {
                    name.text = bird.name
                    description.text = bird.description

                    Glide
                        .with(this.itemView)
                        .load("https://angry-birds-api.herokuapp.com/img/thumbs/${bird.image}.jpg")
                        .centerCrop()
                        .into(image)
                }
            }
            is ListViewHolder.LoadingViewHolder -> {
                notifier.onLastItemReached()
            }
        }
    }

    override fun getItemCount(): Int = birds.count() + 1

    override fun getItemViewType(position: Int): Int {
        return if (position < birds.count())
            ITEM_TYPE_BIRD
        else
            ITEM_TYPE_LOADING
    }

    companion object {
        const val ITEM_TYPE_BIRD = 0;
        const val ITEM_TYPE_LOADING = 1;
    }

    interface LastItemNotifier {
        fun onLastItemReached()
    }
}
