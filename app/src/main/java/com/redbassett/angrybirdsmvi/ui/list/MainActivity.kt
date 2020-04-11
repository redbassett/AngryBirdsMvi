package com.redbassett.angrybirdsmvi.ui.list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.redbassett.angrybirdsmvi.AngryBirdsApp
import com.redbassett.angrybirdsmvi.R
import com.redbassett.angrybirdsmvi.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity<ListState>() {

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
            }
            is Error -> {
                loading_bar.visibility = View.GONE
                Toast
                    .makeText(this, state.error.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }
            is Content -> {
                loading_bar.visibility = View.GONE
                // TODO: 3/31/20 Load content into recyclerview
            }
        }
    }
}
