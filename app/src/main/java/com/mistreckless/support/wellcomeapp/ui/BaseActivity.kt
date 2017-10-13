package com.mistreckless.support.wellcomeapp.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import com.mistreckless.support.wellcomeapp.ui.screen.Layout
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by @mistreckless on 05.08.2017. !
 */


interface BaseActivityView {
    fun lifecycle(): Observable<ActivityEvent>
    fun <T> bindToLifecycle(): LifecycleTransformer<T>
    fun <T> bindUntilEvent(event: ActivityEvent): LifecycleTransformer<T>
}

abstract class BaseActivity<out P : BasePresenter<*,*>,F : BasePresenterProviderFactory<P>> : RxAppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var fragmentDispatcher: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var presenterProviderFactory : F

    val presenter by lazy { presenterProviderFactory.get() }


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        initView()
        presenter.attachRouter(this)
        presenter.attachView(this)
        if (savedInstanceState == null) {
            presenter.onFirstViewAttached()
        } else presenter.onViewRestored(savedInstanceState)
    }


    private fun initView() {
        val cls = javaClass
        if (cls.isAnnotationPresent(Layout::class.java)) {
            val annotation = cls.getAnnotation(Layout::class.java)
            setContentView(annotation.id)
        }
    }

    override fun onDestroy() {
        presenter.detachRouter()
        presenter.detachView()
        super.onDestroy()
    }

    abstract fun setToolbar(toolbar: Toolbar?, isAddedToBackStack: Boolean)

    override fun supportFragmentInjector() = fragmentDispatcher
}

