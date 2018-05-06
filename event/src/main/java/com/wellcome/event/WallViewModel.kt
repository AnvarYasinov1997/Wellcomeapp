package com.wellcome.event

import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import wellcome.common.entity.*

class WallViewModel : ViewModel() {
    private val stateSubject = PublishSubject.create<ItemState>()
    val items by lazy { mutableListOf<EventData>() }

    fun observeState(): Observable<ItemState> = stateSubject

    fun putDocument(eventState: EventState){
        when(eventState){
            is EventAdded ->{
                items.add(0,eventState.event)
                stateSubject.onNext(ItemInserted(0))
            }
            is EventModified ->{
                val newEvent = eventState.event
                val i = items.indexOfFirst { it.ref == newEvent.ref }
                if (i > -1) {
                    val oldEvent = items[i]
                    items[i] = newEvent
                    if (newEvent.eventDataType != oldEvent.eventDataType) {
                        stateSubject.onNext(ItemChanged(i))
                    }
                }
            }
            is EventRemoved ->{
                val i = items.indexOfFirst { it.ref == eventState.ref }
                if (i > -1){
                    items.removeAt(i)
                    stateSubject.onNext(ItemRemoved(i))
                }
            }
            is EventError -> {
                Log.e("event error",eventState.exception.message)
            }
        }
    }
    fun addItems(items: List<EventData>){
        Log.e("viewmodel","events ${items.size}")
        this.items.addAll(items)
        stateSubject.onNext(ItemRangeInserted(this.items.size - items.size,
            this.items.size))
    }

}

sealed class ItemState

data class ItemInserted(val position: Int) : ItemState()
data class ItemRemoved(val position: Int) : ItemState()
data class ItemChanged(val position: Int) : ItemState()
data class ItemRangeInserted(val position: Int, val count: Int) : ItemState()

