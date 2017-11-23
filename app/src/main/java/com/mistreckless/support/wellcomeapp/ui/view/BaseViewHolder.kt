package com.mistreckless.support.wellcomeapp.ui.view

import android.support.annotation.IntegerRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpDelegate
import kotlinx.android.extensions.LayoutContainer

/**
 * Created by @mistreckless on 30.08.2017. !
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseViewHolder<V,out P : BaseViewHolderPresenter<V,T>,T>(parent : ViewGroup?, @IntegerRes id : Int )
    : RecyclerView.ViewHolder(LayoutInflater.from(parent?.context).inflate(id,parent,false)), LayoutContainer{
    abstract val presenter : P
    override val containerView: View? = itemView


    open fun onViewBinded(item: T) {

        presenter.onViewBinded(item,this as V)
    }

    open fun onViewRecycled() {
        presenter.onViewUnbinded()

    }
}