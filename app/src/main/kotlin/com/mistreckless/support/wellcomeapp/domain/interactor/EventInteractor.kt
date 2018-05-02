package com.mistreckless.support.wellcomeapp.domain.interactor

import com.mistreckless.support.wellcomeapp.data.repository.EventRepository
import com.mistreckless.support.wellcomeapp.data.rxfirebase.DocumentState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import wellcome.common.entity.EventData


interface EventInteractor {
    fun controlEvents(observeScroll: Observable<Int>): Observable<List<EventData>>

    fun controlEventChanges(ref: String): Observable<DocumentState<EventData>>

    fun controlEventsChanges(timestamp: Long): Observable<DocumentState<EventData>>
}


class EventInteractorImpl(
    private val eventRepository: EventRepository
) : EventInteractor {
    override fun controlEvents(observeScroll: Observable<Int>): Observable<List<EventData>> {
        var lastTimestamp = 0L
        return observeScroll.distinctUntilChanged()
            .flatMapSingle {
                eventRepository.fetchEvents(lastTimestamp)
                    .doOnSuccess { lastTimestamp = it.lastOrNull()?.timestamp ?: lastTimestamp }
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun controlEventChanges(ref: String): Observable<DocumentState<EventData>> =
        eventRepository.observeEvent(ref)
            .observeOn(AndroidSchedulers.mainThread())

    override fun controlEventsChanges(timestamp: Long): Observable<DocumentState<EventData>> =
        eventRepository.observeEvents(timestamp)
            .observeOn(AndroidSchedulers.mainThread())
}