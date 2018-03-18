package com.mistreckless.support.wellcomeapp.domain.interactor

import com.mistreckless.support.wellcomeapp.data.repository.EventRepository
import com.mistreckless.support.wellcomeapp.data.repository.LocationRepository
import com.mistreckless.support.wellcomeapp.data.repository.UserRepository
import com.mistreckless.support.wellcomeapp.domain.entity.EventData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by mistreckless on 19.10.17.
 */

interface EventInteractor {
    fun controlEvents(observeScroll: Observable<Int>): Observable<List<EventData>>
}


class EventInteractorImpl(private val userRepository: UserRepository, private val eventRepository: EventRepository, private val locationRepository: LocationRepository) : EventInteractor {
    override fun controlEvents(observeScroll: Observable<Int>): Observable<List<EventData>> {
        var lastTimestamp = 0L
        return observeScroll.distinctUntilChanged()
                .flatMapSingle { eventRepository.fetchEvents(lastTimestamp).doOnSuccess { lastTimestamp = it.last().timestamp } }
                .observeOn(AndroidSchedulers.mainThread())
    }

}