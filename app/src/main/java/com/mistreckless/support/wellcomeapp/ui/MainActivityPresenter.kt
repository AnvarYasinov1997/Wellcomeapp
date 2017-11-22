package com.mistreckless.support.wellcomeapp.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.google.android.gms.auth.api.Auth
import com.mistreckless.support.wellcomeapp.domain.entity.AlreadyRegisteredState
import com.mistreckless.support.wellcomeapp.domain.entity.ErrorState
import com.mistreckless.support.wellcomeapp.domain.entity.NewUserState
import com.mistreckless.support.wellcomeapp.domain.interactor.MainInteractor
import com.mistreckless.support.wellcomeapp.ui.screen.profile.Profile
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
class MainActivityPresenter @Inject constructor(private val mainInteractor: MainInteractor, private val rxPermissions: Provider<RxPermissions>, private val router: Router) : BasePresenter<MainActivityView>() {

    override fun onFirstViewAttach() {
        val isGranted = rxPermissions.get().isGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
        val isAuth = mainInteractor.isUserAuthenticated()

        when {
            isGranted && isAuth -> mainInteractor.bindToLocation()
                    .subscribe({
                        viewState.initUi()
                        router.newRootScreen(Wall.TAG)
                    }, {
                        Log.e(TAG, it.message)
                    })
            !isGranted -> viewChangesDisposables.add(rxPermissions.get().request(Manifest.permission.ACCESS_COARSE_LOCATION)
                    .subscribe({
                        if (it && isAuth) {
                            viewState.initUi()
                            router.newRootScreen(Wall.TAG)
                        } else if (it) router.navigateToActivityForResult(BaseActivity.GOOGLE_AUTH_ACTIVITY_TAG, BaseActivity.RC_SIGN_IN)
                        else onFirstViewAttach()
                    }))
            !isAuth -> router.navigateToActivityForResult(BaseActivity.GOOGLE_AUTH_ACTIVITY_TAG, BaseActivity.RC_SIGN_IN)
        }
    }

    fun authResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            BaseActivity.RC_SIGN_IN -> {
                if (resultCode == Activity.RESULT_OK) {
                    val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                    mainInteractor.signUpWithGoogle(result)
                            .subscribe({
                                when (it) {
                                    is AlreadyRegisteredState -> {
                                        Log.e(TAG, "already reg")
                                        viewState.initUi()
                                        router.newRootScreen(Wall.TAG)
                                    }
                                    is NewUserState -> {
                                        router.setResultListener(Registry.RESULT_OK, {
                                            router.removeResultListener(Registry.RESULT_OK)
                                            viewState.initUi()
                                            router.newRootScreen(Wall.TAG)
                                        })
                                        router.newRootScreen(Registry.TAG)
                                    }
                                    is ErrorState -> Log.e(MainActivity.TAG, it.message)
                                }

                            }, { Log.e(MainActivity.TAG, it.message, it) })
                }
            }
        }

    }

    fun wallClicked() = router.newRootScreen(Wall.TAG)

    fun profileClicked() = router.navigateTo(Profile.TAG)

    companion object {
        const val TAG = "MainActivityPresenter"
    }

}


