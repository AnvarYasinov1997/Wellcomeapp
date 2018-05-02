package com.mistreckless.support.wellcomeapp.ui.view

import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.extensions.LayoutContainer

interface BaseRealTimeView<out P : BaseRealTimePresenter<*>, in T, V: Any> : RealTimeVH {
    val presenter: P
    val containerView : View
    var view: V

    fun bind(model: T)
}

abstract class BaseRealTimeViewHolder(view : View) : RecyclerView.ViewHolder(view), RealTimeVH

class DelegateRealTimeViewHolder<out P : BaseRealTimePresenter<T>, in T,V: Any>(
    parent: ViewGroup, @LayoutRes layoutId: Int,
    presenterProvider: BaseRealTimePresenterProvider<P,V>
) :
    RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false)),
    BaseRealTimeView<P,T,V>, LayoutContainer {
    override val containerView: View = itemView

    override lateinit var view: V
    override val presenter by lazy { presenterProvider.providePresenter(view) }

    override fun bind(model: T) = presenter.onViewBinded(model)

    @CallSuper
    override fun onAttach() = presenter.viewAttached()

    @CallSuper
    override fun onDetach() = presenter.viewDetached()
}

abstract class BaseRealTimePresenter<in T> {
    protected val viewDisposable by lazy { CompositeDisposable() }

    abstract fun onViewBinded(item: T)

    abstract fun viewAttached()

    @CallSuper
    open fun viewDetached() {
        viewDisposable.clear()
    }
}

interface BaseRealTimePresenterProvider<out P : BaseRealTimePresenter<*>, in V> {
    fun providePresenter(view: V): P
}


