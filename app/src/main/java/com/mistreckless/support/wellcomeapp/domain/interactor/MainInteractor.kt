package com.mistreckless.support.wellcomeapp.domain.interactor

import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.mistreckless.support.wellcomeapp.domain.entity.AlreadyRegisteredState
import com.mistreckless.support.wellcomeapp.domain.entity.ErrorState
import com.mistreckless.support.wellcomeapp.domain.entity.NewUserState
import com.mistreckless.support.wellcomeapp.domain.entity.RegistryUserState
import com.mistreckless.support.wellcomeapp.domain.repository.UserRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by @mistreckless on 05.08.2017. !
 */
interface MainInteractor {
    fun isUserAuthenticated(): Boolean

    fun signUpWithGoogle(result: GoogleSignInResult): Single<RegistryUserState>
}

class MainInteractorImpl(private val userRepository: UserRepository) : MainInteractor {

    override fun isUserAuthenticated(): Boolean = !userRepository.getUserReference().isEmpty()

    override fun signUpWithGoogle(result: GoogleSignInResult): Single<RegistryUserState> = when (result.isSuccess) {
        true -> {
            val account = result.signInAccount
            userRepository.signUpWithGoogle(account!!)
                    .flatMap { firebaseUser ->
                        userRepository.isUserAlreadyRegistered(firebaseUser.uid)
                                .map { isExists ->
                                    if (isExists) AlreadyRegisteredState()
                                    else NewUserState(firebaseUser.uid, firebaseUser.displayName, firebaseUser.photoUrl.toString())
                                }
                    }
                    .onErrorReturn { ErrorState(it.localizedMessage) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
        false -> Single.just(ErrorState("failed to sign up with google"))
    }

}