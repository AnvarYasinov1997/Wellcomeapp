package com.mistreckless.support.wellcomeapp.domain.interactor

import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import com.mistreckless.support.wellcomeapp.data.repository.LocationRepository
import com.mistreckless.support.wellcomeapp.data.repository.UserRepository
import com.mistreckless.support.wellcomeapp.domain.entity.AlreadyRegisteredState
import com.mistreckless.support.wellcomeapp.domain.entity.ErrorState
import com.mistreckless.support.wellcomeapp.domain.entity.NewUserState
import com.mistreckless.support.wellcomeapp.domain.entity.RegistryUserState
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by @mistreckless on 05.08.2017. !
 */
interface MainInteractor {

    fun signInWithGoogle(account: GoogleSignInAccount): Single<RegistryUserState>

    fun bindToLocation(): Completable
}

class MainInteractorImpl(
    private val userRepository: UserRepository,
    private val locationRepository: LocationRepository
) : MainInteractor {

    override fun signInWithGoogle(account: GoogleSignInAccount): Single<RegistryUserState> {
        Log.e("account", account.displayName)
        userRepository.signInWithGoogle(account)
            .map(FirebaseUser::getUid)
            .flatMap(userRepository::isUserAlreadyRegistered)
            .filter(Boolean::not)

       return userRepository.signInWithGoogle(account)
            .flatMap { firebaseUser ->
                userRepository.isUserAlreadyRegistered(firebaseUser.uid)
                    .filter { it }
                    .map<RegistryUserState> { AlreadyRegisteredState() }
                    .defaultIfEmpty(
                        NewUserState(
                            firebaseUser.uid,
                            firebaseUser.displayName,
                            firebaseUser.photoUrl.toString()
                        )
                    )
                    .toSingle()
            }
            .filter { it is AlreadyRegisteredState }
            .flatMap { bindToLocation().andThen(Maybe.just(it)) }
            .switchIfEmpty(SingleSource { Single.just(it) })
            .onErrorReturn { ErrorState(it.localizedMessage) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }




    override fun bindToLocation(): Completable = locationRepository.getCurrentCity()
        .flatMapCompletable { userRepository.bindToCity() }
        .observeOn(AndroidSchedulers.mainThread())

}