package com.mistreckless.support.wellcomeapp.domain.interactor

import com.mistreckless.support.wellcomeapp.domain.entity.UserData
import com.mistreckless.support.wellcomeapp.data.repository.UserRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by @mistreckless on 02.09.2017. !
 */
interface ProfileInteractor {
    fun controlCurrentUserData(): Observable<UserData>
}


class ProfileInteractorImpl(private val dataInteractor: DataInteractor, private val userRepository: UserRepository) : ProfileInteractor {
    override fun controlCurrentUserData(): Observable<UserData> {
        val userRef = userRepository.getUserReference()
        return dataInteractor.controlUserData(userRef)
                .doOnNext { userRepository.cacheUserData(it) }
                .observeOn(AndroidSchedulers.mainThread())
    }

}