package com.mistreckless.support.wellcomeapp.ui

import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.ui.screen.Layout
import kotlinx.android.synthetic.main.activity_main.*

@Layout(id = R.layout.activity_main, containerId = R.id.container)
class MainActivity : BaseActivity<MainActivityPresenter>(), MainActivityView {

    @InjectPresenter
    override lateinit var presenter: MainActivityPresenter

    @ProvidePresenter
    fun providePresenter(): MainActivityPresenter = presenterProvider.get()


    override fun initUi() {
        navigationView.visibility= View.VISIBLE
        navigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.actionProfile -> presenter.profileClicked()
                R.id.actionWall -> presenter.wallClicked()
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}

interface MainActivityView : BaseActivityView {
    fun initUi()
}
