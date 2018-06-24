package com.wellcomeapp.main

import android.Manifest
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.functions.FirebaseFunctions
import com.tbruyelle.rxpermissions2.RxPermissions
import com.wellcome.core.Cache
import com.wellcome.core.retrofit.Api
import com.wellcome.core.service.StoryService
import com.wellcome.core.ui.BasePresenter
import com.wellcome.core.ui.PerActivity
import com.wellcome.core.ui.Screen
import com.wellcomeapp.main.data.auth.RxAuth
import com.wellcomeapp.main.domain.auth.AuthService
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import ru.terrakok.cicerone.Router
import wellcome.common.core.CacheConst
import wellcome.common.core.InitCityReq
import wellcome.common.entity.CityData
import wellcome.common.entity.LatLon
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
        private val storyService: StoryService,
        private val router: Router,
        private val cache: Cache,
        private val testApi: Api
) : BasePresenter<MainActivityView>() {

    override fun onFirstViewAttach() {
//        launch {
//            val city = cache.getString(CacheConst.USER_CITY, "")
//            Log.e(TAG, city)
//        }
//        launch(UI) {
//            val isAuth = authService.isAuthenticated()
//            if (!isAuth) {
//                val account = auth()
//                val firebaseUser = authService.signInWithGoogle(account).await()
//                if (!authService.checkUserExist(firebaseUser).await())
//                    authService.registryNewUser(firebaseUser).join()
//            }
//
//            val isGranted =
//                    rxPermissions.get().isGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
//            if (isGranted || requestAccess()) {
//                listOf(authService.bindToCity(), storyService.fetchStoriesIfNotExists()).forEach { it.join() }
//                Log.e(TAG, "yeah")
//                storyService.startListen()
//                viewState.initUi()
//                router.newRootScreen(Screen.WALL)
//            }
//        }
        launch {
            //            val city = testApi.helloWorld("52.520008,13.404954", "Berlin").await()
//            Log.e("citytest",city.toString())
            val map = mapOf("lat" to 52.52008, "lon" to 13.404954, "name" to "Berlin")
            FirebaseFunctions.getInstance().getHttpsCallable("initCity")
                    .call(map)
                    .continueWith { task ->
                        if (task.isSuccessful) Log.e("tast", task.result.data.toString() + "suk")
                        else Log.e("err", task.exception?.message)
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