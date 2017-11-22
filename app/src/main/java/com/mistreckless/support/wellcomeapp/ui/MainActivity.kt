package com.mistreckless.support.wellcomeapp.ui

import android.content.Intent
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.ui.screen.Layout
import kotlinx.android.synthetic.main.activity_main.*

@Layout(id = R.layout.activity_main, containerId =  R.id.container)
class MainActivity : BaseActivity<MainActivityPresenter>(), MainActivityView {

    @InjectPresenter
    override lateinit var presenter: MainActivityPresenter

    @ProvidePresenter
    fun providePresenter() = presenterProvider.get()


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.authResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        if (drawerLayout.isMenuVisible)
            drawerLayout.closeMenu(true)
        else
            super.onBackPressed()
    }

    companion object {
        const val TAG = "MainActivity"
    }
}

interface MainActivityView : BaseActivityView
