package com.mistreckless.support.wellcomeapp.ui.screen.wall

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.mistreckless.support.wellcomeapp.data.rxfirebase.*
import wellcome.common.entity.EventData
import io.reactivex.Observable

import io.reactivex.subjects.PublishSubject

class WallViewModel : ViewModel() {
    private val stateSubject = PublishSubject.create<ItemState>()
    val items by lazy { mutableListOf<EventData>() }

    fun observeState(): Observable<ItemState> = stateSubject

    fun putDocument(documentState: DocumentState<EventData>){
        when(documentState){
            is DocumentAdded ->{
                items.add(0,documentState.data)
                stateSubject.onNext(ItemInserted(0))
            }
            is DocumentModified ->{
                val newEvent = documentState.data
                val i = items.indexOfFirst { it.ref == newEvent.ref }
                if (i > -1) {
                    val oldEvent = items[i]
                    items[i] = newEvent
                    if (newEvent.eventDataType != oldEvent.eventDataType) {
                        stateSubject.onNext(ItemChanged(i))
                    }
                }
            }
            is DocumentRemoved ->{
                val i = items.indexOfFirst { it.ref == documentState.ref.id }
                if (i > -1){
                    items.removeAt(i)
                    stateSubject.onNext(ItemRemoved(i))
                }
            }
            is DocumentError -> {
                Log.e("document error",documentState.exception.message)
            }
        }
    }
    fun addItems(items: List<EventData>){
        this.items.addAll(items)
        stateSubject.onNext(ItemRangeInserted(this.items.size - items.size, this.items.size))
    }

}

sealed class ItemState

data class ItemInserted(val position: Int) : ItemState()
data class ItemRemoved(val position: Int) : ItemState()
data class ItemChanged(val position: Int) : ItemState()
data class ItemRangeInserted(val position: Int, val count: Int) : ItemState()

