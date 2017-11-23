package com.mistreckless.support.wellcomeapp.ui.view

import android.support.v7.widget.RecyclerView

/**
 * Created by @mistreckless on 28.08.2017. !
 */
abstract class BaseRealTimeAdapter<T> : RecyclerView.Adapter<BaseViewHolder<*,*,T>>() {
    val items: MutableList<T> by lazy { mutableListOf<T>() }
    private val map: MutableMap<BaseViewHolder<*,*,T>, T> by lazy { mutableMapOf<BaseViewHolder<*,*,T>, T>() }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BaseViewHolder<*,*,T>?, position: Int) {
        if (holder != null) {
            map.put(holder, items[position])
            holder.onViewBinded(items[position])
        }
    }

    override fun onViewRecycled(holder: BaseViewHolder<*,*,T>?) {
        if (holder != null) {
            map.remove(holder)
            holder.onViewRecycled()
        }
    }

    fun onStart() {
        for (v in map.keys) {
            v.onViewRecycled()
            v.onViewBinded(map[v]!!)
        }
    }

    fun onStop() {
        for (v in map.keys) v.onViewRecycled()
    }
}

