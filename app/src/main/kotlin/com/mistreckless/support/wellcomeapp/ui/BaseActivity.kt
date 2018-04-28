package com.mistreckless.support.wellcomeapp.ui

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.MvpView
import com.mistreckless.support.wellcomeapp.navigation.WelcomeNavigator
import com.mistreckless.support.wellcomeapp.ui.screen.Layout
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import ru.terrakok.cicerone.NavigatorHolder
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

    @IdRes
    private var containerId : Int=0

    private val navigator by lazy { WelcomeNavigator(this,supportFragmentManager, containerId)}


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
        val cls = javaClass
        if (cls.isAnnotationPresent(Layout::class.java)) {
            val annotation = cls.getAnnotation(Layout::class.java)
            setContentView(annotation.id)
            containerId=annotation.containerId
        }
    }

    override fun supportFragmentInjector() = fragmentDispatcher
}

