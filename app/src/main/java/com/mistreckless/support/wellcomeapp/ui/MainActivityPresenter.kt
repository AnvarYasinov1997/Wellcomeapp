package com.mistreckless.support.wellcomeapp.ui

import android.Manifest
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.mistreckless.support.wellcomeapp.data.auth.AuthService
import com.mistreckless.support.wellcomeapp.data.auth.RxAuth
import com.mistreckless.support.wellcomeapp.ui.screen.profile.Profile
import com.mistreckless.support.wellcomeapp.ui.screen.wall.Wall
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Maybe
import io.reactivex.Single
import ru.terrakok.cicerone.Router
import javax.inject.Inject
import javax.inject.Provider

@PerActivity
@InjectViewState
class MainActivityPresenter @Inject constructor(
    private val rxPermissions: Provider<RxPermissions>,
    private val rxAuth: Provider<RxAuth>,
    private val authService: AuthService,
    private val router: Router
) : BasePresenter<MainActivityView>() {

    override fun onFirstViewAttach() {
        val isAuth = rxAuth.get().isAuthWithGoogle() && authService.isAuthenticated()

        bindToLocation()
            .map { isAuth }
            .filter(Boolean::not)
            .flatMap { auth() }
            .defaultIfEmpty(true)
            .subscribe({
                Log.e(TAG, it.toString())
            },Throwable::printStackTrace)
    }

    private fun requestAccess() = rxPermissions.get()
        .request(Manifest.permission.ACCESS_COARSE_LOCATION)
        .firstElement()

    private fun bindToLocation() : Single<Unit> {
        val isGranted = rxPermissions.get().isGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
        return Single.just(isGranted)
            .filter(Boolean::not)
            .flatMap { requestAccess() }
            .defaultIfEmpty(true)
            .toSingle()
            .flatMap{authService.bindToCity()}
    }

    private fun auth() = rxAuth.get()
        .authWithGoogle()
        .flatMap(authService::signInWithGoogle)
        .flatMapMaybe { Maybe.just(true) }


    fun wallClicked() = router.newRootScreen(Wall.TAG)

    fun profileClicked() = router.navigateTo(Profile.TAG)


    companion object {
        const val TAG = "MainActivityPresenter"
    }

}

fun Boolean.value() = this
