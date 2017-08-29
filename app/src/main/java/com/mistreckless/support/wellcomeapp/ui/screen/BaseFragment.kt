package com.mistreckless.support.wellcomeapp.ui.screen

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import com.mistreckless.support.wellcomeapp.ui.BaseActivity
import com.mistreckless.support.wellcomeapp.ui.BasePresenter
import com.mistreckless.support.wellcomeapp.ui.MainActivityRouter
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.components.support.RxFragment
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by @mistreckless on 31.07.2017. !
 */

interface BaseFragmentView {
    fun lifecycle(): Observable<FragmentEvent>
    fun <T> bindToLifecycle(): LifecycleTransformer<T>
    fun <T> bindUntilEvent(event: FragmentEvent): LifecycleTransformer<T>
}

abstract class BaseFragment<P : BasePresenter<*, *>> : RxFragment() {
    @Inject
    lateinit var presenter: P

    var unbinder: Unbinder? = null
    var restoredBundle : Bundle?=null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        retainInstance = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        restoredBundle = savedInstanceState
        super.onCreate(savedInstanceState)
        presenter.attachRouter(getRouter())
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val cls = javaClass
        if (!cls.isAnnotationPresent(Layout::class.java)) return null
        val annotation = cls.getAnnotation(Layout::class.java)
        val view = inflater!!.inflate(annotation.id, container, false)
        unbinder = ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        presenter.attachView(this)
        presenter.attachRouter(getRouter())

        (activity as BaseActivity<*>).setToolbar(getCurrentToolbar(), isAddedToBackStack())

        if (restoredBundle !=null)
            presenter.onViewRestored(restoredBundle!!)
        else presenter.onFirstViewAttached()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        presenter.attachRouter(getRouter())
        super.onActivityCreated(savedInstanceState)
    }

    override fun onDestroyView() {
        presenter.detachView()
        unbinder?.unbind()
        super.onDestroyView()
    }

    override fun onDestroy() {
        presenter.detachRouter()
        super.onDestroy()
    }

    protected fun getRouter(): Any = activity as MainActivityRouter
    protected fun isAddedToBackStack() = false

    abstract fun getCurrentToolbar(): Toolbar?
}