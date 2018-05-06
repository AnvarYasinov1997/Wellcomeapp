package com.mistreckless.support.wellcomeapp.ui.view

import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.extensions.LayoutContainer

abstract class RealTimeViewHolder<out PRESENTER : RealTimePresenter<MODEL>, in MODEL, VIEW>(parent: ViewGroup, @LayoutRes layoutId: Int,
                                                                                              presenterProvider: BaseRealTimePresenterProvider<PRESENTER, VIEW>) :
    RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false)), RealTimeVH {

    abstract val view: VIEW
    val presenter by lazy { presenterProvider.providePresenter(view) }

    fun bind(model: MODEL) = presenter.onViewBinded(model)

    @CallSuper
    override fun onAttach() = presenter.viewAttached()

    @CallSuper
    override fun onDetach() = presenter.viewDetached()

}

interface RealTimePresenter<in T> {

    fun onViewBinded(item: T)

    fun viewAttached()

    fun viewDetached()
}

interface BaseRealTimePresenterProvider<out P : RealTimePresenter<*>, in V> {
    fun providePresenter(view: V): P
}