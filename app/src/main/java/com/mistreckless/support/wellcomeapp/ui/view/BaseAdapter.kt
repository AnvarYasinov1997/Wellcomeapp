package com.mistreckless.support.wellcomeapp.ui.view

import android.support.v7.widget.RecyclerView

/**
 * Created by @mistreckless on 28.08.2017. !
 */
abstract class BaseAdapter<M : Any> : RecyclerView.Adapter<BaseViewHolder<*,*>>() {
    var items: MutableList<M> = mutableListOf()

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BaseViewHolder<*, *>?, position: Int) {
        holder?.onViewBinded(items[position])
    }

    override fun onViewRecycled(holder: BaseViewHolder<*,*>?) {
        super.onViewRecycled(holder)
        holder?.onViewRecycled()
    }

}

