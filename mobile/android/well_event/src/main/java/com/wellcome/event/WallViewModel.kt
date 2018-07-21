package com.wellcome.event

import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import wellcome.common.event.*
import wellcome.common.mpp.entity.Event

class WallViewModel : ViewModel() {
    private val stateSubject = PublishSubject.create<ItemState>()
    val items by lazy { mutableListOf<Event>() }

    fun observeState(): Observable<ItemState> = stateSubject

    fun putState(eventState: EventState) {
        when (eventState) {
            is EventAdded      -> {
                items.add(0, eventState.event)
                stateSubject.onNext(ItemInserted(0))
            }
            is EventModified   -> {
                val newEvent = eventState.event
                val i = items.indexOfFirst { it.data.ref == newEvent.data.ref }
                if (i > -1) {
                    val oldEvent = items[i]
                    items[i] = newEvent
                    if (newEvent.data.eventDataType != oldEvent.data.eventDataType) {
                        stateSubject.onNext(ItemChanged(i))
                    }
                }
            }
            is EventRemoved    -> {
                val i = items.indexOfFirst { it.data.ref == eventState.ref }
                if (i > -1) {
                    items.removeAt(i)
                    stateSubject.onNext(ItemRemoved(i))
                }
            }
            is UserModified    -> {
            }
            is EventsPaginated -> {
                items.addAll(eventState.events)
                stateSubject.onNext(ItemRangeInserted(items.size - eventState.events.size,
                    items.size))
            }
            is StateError      -> Log.e("event error", eventState.message)

        }
    }
}

sealed class ItemState

data class ItemInserted(val position: Int) : ItemState()
data class ItemRemoved(val position: Int) : ItemState()
data class ItemChanged(val position: Int) : ItemState()
data class ItemRangeInserted(val position: Int, val count: Int) : ItemState()

