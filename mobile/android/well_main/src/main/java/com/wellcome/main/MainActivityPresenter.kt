package com.wellcome.main

import android.Manifest
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.tbruyelle.rxpermissions2.RxPermissions
import com.wellcome.core.navigation.Screen
import com.wellcome.main.data.auth.RxAuth
import com.wellcome.main.domain.auth.AuthService
import com.wellcome.ui.core.BasePresenter
import com.wellcome.ui.core.PerActivity
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.joinAll
import kotlinx.coroutines.experimental.launch
import ru.terrakok.cicerone.Router
import wellcome.common.core.service.StoryService
import javax.inject.Inject
import javax.inject.Provider
import kotlin.coroutines.experimental.suspendCoroutine

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
@PerActivity
@InjectViewState
class MainActivityPresenter @Inject constructor(private val rxPermissions: Provider<RxPermissions>,
                                                private val rxAuth: Provider<RxAuth>,
                                                private val authService: AuthService,
                                                private val storyService: StoryService,
                                                private val router: Router) :
    BasePresenter<MainActivityView>() {

    override fun onFirstViewAttach() {
        launch(UI) {
            val account = auth()
            val firebaseUser = authService.signInWithGoogle(account).await()
            authService.bindUser(firebaseUser).join()

            val isGranted =
                rxPermissions.get().isGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
            if (isGranted || requestAccess()) {
                joinAll(authService.bindToCity(), storyService.fetchStoriesIfNotExists())
                Log.e(TAG, "yeah")
                storyService.startListen()
                viewState.initUi()
                router.newRootScreen(Screen.WALL)
            }
        }
    }

    private suspend fun requestAccess() = suspendCoroutine<Boolean> { cont ->
        rxPermissions.get().request(Manifest.permission.ACCESS_COARSE_LOCATION).firstElement()
                .subscribe {
                    cont.resume(it)
                }
    }

    private suspend fun auth() = suspendCoroutine<GoogleSignInAccount> { cont ->
        rxAuth.get().authWithGoogle().subscribe({
                    cont.resume(it)
                }, {
                    Log.e(TAG, it.message, it)
                    cont.resumeWithException(it)
                })
    }

    fun wallClicked() = router.newRootScreen(Screen.WALL)

    fun profileClicked() = router.navigateTo(Screen.PROFILE)


    companion object {
        const val TAG = "MainActivityPresenter"
    }

}