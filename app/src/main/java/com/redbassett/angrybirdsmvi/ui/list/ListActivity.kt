package com.redbassett.angrybirdsmvi.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.redbassett.angrybirdsmvi.AngryBirdsApp
import com.redbassett.angrybirdsmvi.R
import com.redbassett.angrybirdsmvi.data.model.Bird
import com.redbassett.angrybirdsmvi.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class ListActivity : BaseActivity<ListState>() {

    @Inject lateinit var presenter: ListPresenter

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AngryBirdsApp).appComponent.inject(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        presenter.bindView(this)

        viewManager = LinearLayoutManager(this)
        viewAdapter = BirdListViewAdapter()
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
                Toast
                    .makeText(this, state.error.localizedMessage, Toast.LENGTH_LONG)
                    .show()
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
}

class BirdListViewAdapter
    : RecyclerView.Adapter<BirdListViewAdapter.BirdViewHolder>() {

    var birds: List<Bird> = arrayListOf()

    class BirdViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.bird_name)
        val description: TextView = view.findViewById(R.id.bird_description)
        val image: ImageView = view.findViewById(R.id.bird_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BirdViewHolder {
        val rootView = LayoutInflater.from(parent.context)
            .inflate(R.layout.bird_list_item, parent, false) as ConstraintLayout

        return BirdViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: BirdViewHolder, position: Int) {
        val bird = birds[position]
        holder.apply {
            name.text = bird.name
            description.text = bird.description

            Glide
                .with(this.itemView)
                .load("https://redbassett-angrybirds.builtwithdark.com/img/${bird.image}.jpg")
                .centerCrop()
                .into(image)
        }
    }

    override fun getItemCount(): Int = birds.count()
}
