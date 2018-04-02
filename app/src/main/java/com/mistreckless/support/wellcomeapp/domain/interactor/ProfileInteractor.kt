package com.mistreckless.support.wellcomeapp.domain.interactor

import com.mistreckless.support.wellcomeapp.domain.entity.UserData
import com.mistreckless.support.wellcomeapp.data.repository.UserRepository
import com.mistreckless.support.wellcomeapp.data.rxfirebase.DocumentState
import com.mistreckless.support.wellcomeapp.data.rxfirebase.DocumentModified
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

interface ProfileInteractor {
    fun controlCurrentUserData(): Observable<DocumentState<UserData>>
}


class ProfileInteractorImpl(
    private val dataInteractor: DataInteractor,
    private val userRepository: UserRepository
) : ProfileInteractor {
    override fun controlCurrentUserData(): Observable<DocumentState<UserData>> {
        val userRef = userRepository.getUserReference()
        return dataInteractor.controlUserData(userRef)
            .doOnNext(this::sideCache)
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun sideCache(state: DocumentState<UserData>) {
        when (state) {
            is DocumentModified -> userRepository.cacheUserData(state.data)
        }
    }
}