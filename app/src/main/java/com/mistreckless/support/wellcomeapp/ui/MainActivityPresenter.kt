package com.mistreckless.support.wellcomeapp.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.api.GoogleApiClient
import com.mistreckless.support.wellcomeapp.domain.entity.AlreadyRegisteredState
import com.mistreckless.support.wellcomeapp.domain.entity.ErrorState
import com.mistreckless.support.wellcomeapp.domain.entity.NewUserState
import com.mistreckless.support.wellcomeapp.domain.interactor.MainInteractor
import com.mistreckless.support.wellcomeapp.ui.screen.registry.Registry
import com.mistreckless.support.wellcomeapp.ui.screen.wall.Wall
import com.tbruyelle.rxpermissions2.RxPermissions
import ru.terrakok.cicerone.Router
import javax.inject.Inject
import javax.inject.Provider


/**
 * Created by @mistreckless on 30.07.2017. !
 */

@PerActivity
@InjectViewState
class MainActivityPresenter @Inject constructor(private val mainInteractor: MainInteractor, private val rxPermissions: Provider<RxPermissions>, private val router : Router) : BasePresenter<MainActivityView>() {

    override fun onFirstViewAttach() {
        val isGranted = rxPermissions.get().isGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
        val isAuth = mainInteractor.isUserAuthenticated()

        when {
            isGranted && isAuth -> mainInteractor.bindToLocation()
                    .subscribe({
                        router.navigateTo(Wall.TAG)
                    }, {
                        Log.e(TAG, it.message)
                    })
            !isGranted -> viewChangesDisposables.add(rxPermissions.get().request(Manifest.permission.ACCESS_COARSE_LOCATION)
                    .subscribe({
                        if (it && isAuth) router.navigateTo(Wall.TAG)
                        else if (it) getRouter()?.navigateToGoogleAuthDialog(googleApiClient.get())
                        else onFirstViewAttach()
                    }))
            !isAuth -> getRouter()?.navigateToGoogleAuthDialog(googleApiClient.get())
        }
    }

    fun authResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            MainActivity.RC_SIGN_IN -> {
                if (resultCode == Activity.RESULT_OK) {
                    val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                    mainInteractor.signUpWithGoogle(result)
                            .subscribe({
                                when (it) {
                                    is AlreadyRegisteredState -> {
                                        Log.e(TAG, "already reg")
                                        router.navigateTo(Wall.TAG)
                                    }
                                    is NewUserState -> router.navigateTo(Registry.TAG,it)
                                    is ErrorState -> Log.e(MainActivity.TAG, it.message)
                                }

                            }, { Log.e(MainActivity.TAG, it.message,it) })
                }
            }
        }

    }

    companion object {
        const val TAG = "MainActivityPresenter"
    }

}


