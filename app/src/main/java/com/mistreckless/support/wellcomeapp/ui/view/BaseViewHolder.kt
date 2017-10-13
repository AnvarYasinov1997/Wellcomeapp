package com.mistreckless.support.wellcomeapp.ui.view

import android.support.annotation.IntegerRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer

/**
 * Created by @mistreckless on 30.08.2017. !
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseViewHolder< M, P : BaseViewHolderPresenter<*, *, M>>(private val router: Any, parent : ViewGroup?, @IntegerRes id : Int )
    : RecyclerView.ViewHolder(LayoutInflater.from(parent?.context).inflate(id,parent,false)), LayoutContainer{
    abstract var presenter : P
    override val containerView: View?
        get() = itemView

    open fun onViewBinded(item: Any) {
        presenter.attachRouter(router)
        presenter.attachView(this)
        presenter.onViewBinded(item as M)
    }

    open fun onViewRecycled() {
        presenter.detachView()
        presenter.onViewUnbinded()
        presenter.detachRouter()
    }
}