package com.wellcomeapp.ui_core

import android.os.Bundle
import android.support.v4.app.Fragment
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.MvpView
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.SupportAppNavigator
import javax.inject.Inject
import javax.inject.Provider

interface BaseActivityView : MvpView

abstract class BaseActivity<P : BasePresenter<*>> : MvpAppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentDispatcher: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var presenterProvider: Provider<P>

    @Inject
    lateinit var navigatorHolder : NavigatorHolder

    abstract val presenter: P

    abstract val layoutId : Int

    abstract val navigator: SupportAppNavigator// by lazy { WelcomeNavigator(this,supportFragmentManager, containerId)}


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }


    private fun initView() {
        setContentView(layoutId)
    }

    override fun supportFragmentInjector() = fragmentDispatcher
}

interface ParentContainer{
    fun hideBottom()
    fun showBottom()
}