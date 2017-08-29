package com.mistreckless.support.wellcomeapp.domain.interactor

import android.content.Intent
import com.mistreckless.support.wellcomeapp.domain.entity.NewUserState
import com.mistreckless.support.wellcomeapp.domain.repository.LocationRepository
import com.mistreckless.support.wellcomeapp.domain.repository.UserRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by @mistreckless on 13.08.2017. !
 */
interface RegistryInteractor {
    fun findMyCity(): Single<String>
    fun regUser(name: String, newUserState: NewUserState): Completable
    fun choosePhoto(data: Intent): Single<String>

}

class RegistryInteractorImpl(private val userRepository: UserRepository, private val locationRepository: LocationRepository) : RegistryInteractor {
    override fun findMyCity(): Single<String> = locationRepository.getCurrentCity()
            .observeOn(AndroidSchedulers.mainThread())

    override fun regUser(name: String, newUserState: NewUserState): Completable {
        return userRepository.uploadPhotoIfNeeded()
                .flatMapCompletable { userRepository.registryNewUser(newUserState.uid, newUserState.fullName, if (name.isEmpty()) newUserState.fullName ?: "noname" else name, it) }
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun choosePhoto(data: Intent): Single<String> =
            userRepository.findPhoto(data.data)
                    .observeOn(AndroidSchedulers.mainThread())

}