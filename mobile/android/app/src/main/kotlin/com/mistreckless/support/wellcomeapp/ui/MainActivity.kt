package com.mistreckless.support.wellcomeapp.ui

import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.navigation.WelcomeNavigator
import com.wellcome.core.ui.BaseActivity
import com.wellcome.core.ui.BaseActivityView
import com.wellcome.core.ui.ParentContainer
import kotlinx.android.synthetic.main.activity_main.*
import ru.terrakok.cicerone.android.SupportAppNavigator

class MainActivity : BaseActivity<MainActivityPresenter>(), MainActivityView, ParentContainer {
    override val layoutId: Int
        get() = R.layout.activity_main
    override val navigator: SupportAppNavigator
        get() = WelcomeNavigator(this, supportFragmentManager, R.id.container)

    private val height by lazy { navigationView.height }

    @InjectPresenter
    override lateinit var presenter: MainActivityPresenter

    @ProvidePresenter
    fun providePresenter(): MainActivityPresenter = presenterProvider.get()

    override fun initUi() {
        navigationView.visibility = View.VISIBLE
        navigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.actionProfile -> presenter.profileClicked()
                R.id.actionWall -> presenter.wallClicked()
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun showBottom() {
        navigationView.clearAnimation()
        navigationView.animate().translationY(0f).duration = 200
    }

    override fun hideBottom() {
        navigationView.clearAnimation()
        navigationView.animate().translationY(height.toFloat()).duration = 200
    }

    companion object {
        const val TAG = "MainActivity"
    }
}

interface MainActivityView : BaseActivityView {
    fun initUi()
}
