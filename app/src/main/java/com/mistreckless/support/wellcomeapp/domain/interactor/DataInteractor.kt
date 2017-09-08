package com.mistreckless.support.wellcomeapp.domain.interactor

import com.jakewharton.rx.replayingShare
import com.mistreckless.support.wellcomeapp.domain.entity.RatingData
import com.mistreckless.support.wellcomeapp.domain.entity.UserData
import com.mistreckless.support.wellcomeapp.domain.repository.UserRepository
import io.reactivex.Observable
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

/**
 * Created by @mistreckless on 02.09.2017. !
 */
interface DataInteractor {
    fun controlUserData(userRef: String): Observable<UserData>

    fun controlRatingData(ratingRef: String): Observable<RatingData>
}

class DataInteractorImpl(private val userRepository: UserRepository) : DataInteractor {

    private val userMap: ConcurrentMap<String, Observable<UserData>> by lazy { ConcurrentHashMap<String, Observable<UserData>>() }
    private val ratingMap: ConcurrentHashMap<String, Observable<RatingData>> by lazy { ConcurrentHashMap<String, Observable<RatingData>>() }

    override fun controlUserData(userRef: String): Observable<UserData> {
        var observable = userMap[userRef]
        if (observable == null) {
            observable = userRepository.observeUserValueEvent(userRef)
                    .replayingShare()
                    .doOnDispose { userMap.remove(userRef) }
            userMap.put(userRef, observable)
        }
        return observable!!


    }

    override fun controlRatingData(ratingRef: String): Observable<RatingData> {
        var observable = ratingMap[ratingRef]
        if (observable == null) {
            observable = userRepository.observeRatingValueEvent(ratingRef)
                    .replayingShare()
                    .doOnDispose { ratingMap.remove(ratingRef) }
            ratingMap.put(ratingRef, observable)
        }
        return observable!!
    }

}