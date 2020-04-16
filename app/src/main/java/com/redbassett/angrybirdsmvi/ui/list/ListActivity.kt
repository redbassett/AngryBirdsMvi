package com.redbassett.angrybirdsmvi.ui.list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.redbassett.angrybirdsmvi.AngryBirdsApp
import com.redbassett.angrybirdsmvi.R
import com.redbassett.angrybirdsmvi.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class ListActivity : BaseActivity<ListState>() {

    @Inject lateinit var presenter: ListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AngryBirdsApp).appComponent.inject(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        presenter.bindView(this)
    }

    override fun render(state: ListState) {
        when (state) {
            is Loading -> {
                loading_bar.visibility = View.VISIBLE
                error_group.visibility = View.GONE
            }
            is Error -> {
                loading_bar.visibility = View.GONE
                error_group.visibility = View.VISIBLE
                error_message.text = state.error.localizedMessage
                Toast
                    .makeText(this, state.error.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }
            is Content -> {
                loading_bar.visibility = View.GONE
                error_group.visibility = View.GONE
                Toast
                    .makeText(this, "Content loaded", Toast.LENGTH_LONG)
                    .show()
                // TODO: 3/31/20 Load content into recyclerview
            }
        }
    }
}
