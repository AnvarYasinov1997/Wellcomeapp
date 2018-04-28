package com.mistreckless.support.wellcomeapp.ui.screen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.MvpView
import com.mistreckless.support.wellcomeapp.ui.BasePresenter
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject
import javax.inject.Provider

interface BaseFragmentView : MvpView

abstract class BaseFragment<P : BasePresenter<*>> : MvpAppCompatFragment() {

    @Inject
    lateinit var presenterProvider: Provider<P>

    abstract val presenter: P

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val cls = javaClass
        if (!cls.isAnnotationPresent(Layout::class.java)) return null
        val annotation = cls.getAnnotation(Layout::class.java)
        return inflater.inflate(annotation.id, container, false)
    }

}