package com.mistreckless.support.wellcomeapp.ui

import android.Manifest
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.mistreckless.support.wellcomeapp.data.auth.RxAuth
import com.mistreckless.support.wellcomeapp.domain.auth.AuthService
import com.tbruyelle.rxpermissions2.RxPermissions
import com.wellcome.utils.ui.BasePresenter
import com.wellcome.utils.ui.PerActivity
import com.wellcome.utils.ui.Screen
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import ru.terrakok.cicerone.Router
import wellcome.common.cache.Cache
import wellcome.common.cache.CacheConst
import javax.inject.Inject
import javax.inject.Provider
import kotlin.coroutines.experimental.suspendCoroutine

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
@PerActivity
@InjectViewState
class MainActivityPresenter @Inject constructor(
    private val rxPermissions: Provider<RxPermissions>,
    private val rxAuth: Provider<RxAuth>,
    private val authService: AuthService,
    private val router: Router,
    private val cache: Cache
) : BasePresenter<MainActivityView>() {

    override fun onFirstViewAttach() {
        launch {
            val city = cache.getString(CacheConst.USER_CITY, "")
            Log.e(TAG, city)
        }
        launch(UI) {
            val isAuth = authService.isAuthenticated()
            if (!isAuth) {
                val account = auth()
                val firebaseUser = authService.signInWithGoogle(account).await()
                if (!authService.checkUserExist(firebaseUser).await())
                    authService.registryNewUser(firebaseUser).join()
            }

            val isGranted =
                rxPermissions.get().isGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
            if (isGranted || requestAccess()) {
                authService.bindToCity().join()
                Log.e(TAG, "yeah")
                viewState.initUi()
                router.newRootScreen(Screen.WALL)
            }
        }
    }

    private suspend fun requestAccess() = suspendCoroutine<Boolean> { cont ->
        rxPermissions.get()
            .request(Manifest.permission.ACCESS_COARSE_LOCATION)
            .firstElement()
            .subscribe {
                cont.resume(it)
            }
    }

    private suspend fun auth() = suspendCoroutine<GoogleSignInAccount> { cont ->
        rxAuth.get()
            .authWithGoogle()
            .subscribe({
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