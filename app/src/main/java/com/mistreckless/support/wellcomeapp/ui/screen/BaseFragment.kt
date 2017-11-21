package com.mistreckless.support.wellcomeapp.ui.screen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.MvpView
import com.mistreckless.support.wellcomeapp.ui.BasePresenter
import com.mistreckless.support.wellcomeapp.ui.MainActivityRouter
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by @mistreckless on 31.07.2017. !
 */

interface BaseFragmentView : MvpView

abstract class BaseFragment<P : BasePresenter<*, *>> : MvpAppCompatFragment() {


    @Inject
    lateinit var presenterProvider: Provider<P>

    abstract val presenter: P

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachRouter(getRouter())
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val cls = javaClass
        if (!cls.isAnnotationPresent(Layout::class.java)) return null
        val annotation = cls.getAnnotation(Layout::class.java)
        return inflater.inflate(annotation.id, container, false)
    }


    override fun onDestroy() {
        presenter.detachRouter()
        super.onDestroy()
    }

    protected open fun getRouter(): Any = activity as MainActivityRouter

}