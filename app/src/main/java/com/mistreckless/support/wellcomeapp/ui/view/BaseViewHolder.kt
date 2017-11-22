package com.mistreckless.support.wellcomeapp.ui.view

import android.support.annotation.IntegerRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentReference
import kotlinx.android.extensions.LayoutContainer

/**
 * Created by @mistreckless on 30.08.2017. !
 */
abstract class BaseViewHolder<P : BaseViewHolderPresenter<*>>(parent : ViewGroup?, @IntegerRes id : Int )
    : RecyclerView.ViewHolder(LayoutInflater.from(parent?.context).inflate(id,parent,false)), LayoutContainer{
    abstract var presenter : P
    override val containerView: View? = itemView

    open fun onViewBinded(item: DocumentReference) {
        presenter.onViewBinded(item)
    }

    open fun onViewRecycled() {
        presenter.onViewUnbinded()
    }
}