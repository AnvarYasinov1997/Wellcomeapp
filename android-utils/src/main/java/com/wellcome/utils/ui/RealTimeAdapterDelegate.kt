package com.wellcome.utils.ui

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent


interface RealTimeAdapter<in VH : RealTimeVH>{
    fun onViewAttachedToWindow(holder: VH)
    fun onViewDetachedFromWindow(holder: VH)
}

class RealTimeAdapterDelegate<in VH : RealTimeVH>(private val lifecycle: Lifecycle) : RealTimeAdapter<VH>, LifecycleObserver{
    init {
        lifecycle.addObserver(this)
    }
    private val holders = mutableListOf<RealTimeVH>()

    override fun onViewAttachedToWindow(holder: VH) {
        holders.add(holder)
        holder.onAttach()
    }

    override fun onViewDetachedFromWindow(holder: VH) {
        holder.onDetach()
        holders.remove(holder)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() = lifecycle.removeObserver(this)

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() = holders.forEach { it.onAttach() }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() = holders.forEach { it.onDetach() }

}

interface RealTimeVH{
    fun onAttach()
    fun onDetach()
}
