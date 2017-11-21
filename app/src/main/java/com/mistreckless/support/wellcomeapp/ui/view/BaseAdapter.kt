package com.mistreckless.support.wellcomeapp.ui.view

import android.support.v7.widget.RecyclerView
import com.google.firebase.firestore.DocumentReference

/**
 * Created by @mistreckless on 28.08.2017. !
 */
abstract class BaseRealTimeAdapter : RecyclerView.Adapter<BaseViewHolder<*>>() {
    val items: MutableList<DocumentReference> by lazy { mutableListOf<DocumentReference>() }
    private val map: MutableMap<BaseViewHolder<*>, DocumentReference> by lazy { mutableMapOf<BaseViewHolder<*>, DocumentReference>() }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BaseViewHolder<*>?, position: Int) {
        if (holder != null) {
            map.put(holder, items[position])
            holder.onViewBinded(items[position])
        }
    }

    override fun onViewRecycled(holder: BaseViewHolder<*>?) {
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

