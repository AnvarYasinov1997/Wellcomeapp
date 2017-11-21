package com.mistreckless.support.wellcomeapp.domain.interactor

import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.mistreckless.support.wellcomeapp.data.repository.LocationRepository
import com.mistreckless.support.wellcomeapp.data.repository.UserRepository
import com.mistreckless.support.wellcomeapp.domain.entity.AlreadyRegisteredState
import com.mistreckless.support.wellcomeapp.domain.entity.ErrorState
import com.mistreckless.support.wellcomeapp.domain.entity.NewUserState
import com.mistreckless.support.wellcomeapp.domain.entity.RegistryUserState
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by @mistreckless on 05.08.2017. !
 */
interface MainInteractor {
    fun isUserAuthenticated(): Boolean

    fun signUpWithGoogle(result: GoogleSignInResult): Single<RegistryUserState>

    fun bindToLocation(): Completable
}

class MainInteractorImpl(private val userRepository: UserRepository, private val locationRepository: LocationRepository) : MainInteractor {

    override fun isUserAuthenticated(): Boolean = userRepository.getUserReference().isNotEmpty() && userRepository.isSignInToGoogle()

    override fun signUpWithGoogle(result: GoogleSignInResult): Single<RegistryUserState> = when (result.isSuccess) {
        true -> {
            val account = result.signInAccount
            Log.e("account",account!!.displayName)
            userRepository.signUpWithGoogle(account)
                    .flatMap { firebaseUser ->
                        userRepository.isUserAlreadyRegistered(firebaseUser.uid)
                                .map { isExists ->
                                    if (isExists) AlreadyRegisteredState()
                                    else NewUserState(firebaseUser.uid, firebaseUser.displayName, firebaseUser.photoUrl.toString())
                                }
                    }
                    .flatMap { if (it is AlreadyRegisteredState) bindToLocation().andThen(Single.just(it)) else Single.just(it) }
                    .onErrorReturn { ErrorState(it.localizedMessage) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
        false -> Single.just(ErrorState("failed to sign up with google"))
    }

    override fun bindToLocation(): Completable = locationRepository.getCurrentCity()
            .toCompletable()
            .andThen { userRepository.bindToCity() }
            .observeOn(AndroidSchedulers.mainThread())

}