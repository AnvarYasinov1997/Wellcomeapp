package com.mistreckless.support.wellcomeapp.domain.interactor

import com.jakewharton.rx.replayingShare
import wellcome.common.entity.UserData
import com.mistreckless.support.wellcomeapp.data.repository.UserRepository
import com.mistreckless.support.wellcomeapp.data.rxfirebase.DocumentState
import io.reactivex.Observable
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

/**
 * Created by @mistreckless on 02.09.2017. !
 */
interface DataInteractor {
    fun controlUserData(userRef: String): Observable<DocumentState<UserData>>

}

class DataInteractorImpl(private val userRepository: UserRepository) : DataInteractor {

    private val userMap: ConcurrentMap<String, Observable<DocumentState<UserData>>> by lazy { ConcurrentHashMap<String, Observable<DocumentState<UserData>>>() }

    override fun controlUserData(userRef: String): Observable<DocumentState<UserData>> {
        var observable = userMap[userRef]
        if (observable == null) {
            observable = userRepository.observeUserValueEvent(userRef)
                .replayingShare()
                .doOnDispose { userMap.remove(userRef) }
            userMap.put(userRef, observable)
        }
        return observable!!


    }

}