package com.mistreckless.support.wellcomeapp.domain.interactor

import com.mistreckless.support.wellcomeapp.domain.entity.RatingData
import com.mistreckless.support.wellcomeapp.domain.entity.UserData
import com.mistreckless.support.wellcomeapp.domain.repository.UserRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by @mistreckless on 02.09.2017. !
 */
interface ProfileInteractor {
    fun controlCurrentUserData(): Observable<UserData>

    fun controlCurrentRatingData(): Observable<RatingData>
}


class ProfileInteractorImpl(private val dataInteractor: DataInteractor, private val userRepository: UserRepository) : ProfileInteractor {
    override fun controlCurrentUserData(): Observable<UserData> {
        val userRef = userRepository.getUserReference()
        return dataInteractor.controlUserData(userRef)
                .doOnNext { userRepository.cacheUserData(it) }
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun controlCurrentRatingData(): Observable<RatingData> {
        val ratingRef = userRepository.getRatingReference()
        return dataInteractor.controlRatingData(ratingRef)
                .doOnNext { userRepository.cacheRatingData(it) }
                .observeOn(AndroidSchedulers.mainThread())
    }

}