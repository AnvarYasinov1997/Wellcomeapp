package com.mistreckless.support.wellcomeapp.ui.view

import android.support.annotation.IntegerRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder

/**
 * Created by @mistreckless on 30.08.2017. !
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseViewHolder< M, P : BaseViewHolderPresenter<*, *, M>>(private val router: Any, parent : ViewGroup?, @IntegerRes id : Int )
    : RecyclerView.ViewHolder(LayoutInflater.from(parent?.context).inflate(id,parent,false)){
    var unbinder: Unbinder? = null
    abstract var presenter : P

    fun onViewBinded(item: Any) {
        presenter.attachRouter(router)
        unbinder= ButterKnife.bind(this,itemView)
        presenter.attachView(this)
        presenter.onViewBinded(item as M)
    }

    fun onViewRecycled() {
        presenter.detachView()
        presenter.onViewUnbinded()
        unbinder?.unbind()
        presenter.detachRouter()
    }
}