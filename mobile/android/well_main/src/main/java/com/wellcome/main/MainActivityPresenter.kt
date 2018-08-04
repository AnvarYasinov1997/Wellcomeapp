package com.wellcome.main

import android.Manifest
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.wellcome.core.navigation.Screen
import com.wellcome.main.domain.auth.AuthService
import com.wellcome.ui.core.BasePresenter
import com.wellcome.ui.core.PerActivity
import com.wellcome.utils.RationaleDialog
import com.wellcome.utils.auth.AuthResult
import com.wellcome.utils.auth.GoogleAuth
import com.wellcome.utils.permission.AppSettingsPage
import com.wellcome.utils.permission.AsyncPermissions
import com.wellcome.utils.permission.PermissionResult
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.joinAll
import kotlinx.coroutines.experimental.launch
import ru.terrakok.cicerone.Router
import wellcome.common.core.service.StoryService
import javax.inject.Inject
import javax.inject.Provider

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
@PerActivity
@InjectViewState
class MainActivityPresenter @Inject constructor(private val permissions: Provider<AsyncPermissions>,
                                                private val rationaleDialog: Provider<RationaleDialog>,
                                                private val appSettingsPage: Provider<AppSettingsPage>,
                                                private val googleAuth: Provider<GoogleAuth>,
                                                private val authService: AuthService,
                                                private val storyService: StoryService,
                                                private val router: Router) :
    BasePresenter<MainActivityView>() {

    override fun onFirstViewAttach() {
        launch(UI) {
            val account = requestAuth()
            val firebaseUser = authService.signInWithGoogle(account).await()
            authService.bindUser(firebaseUser).join()
            requestLocation()

            joinAll(authService.bindToCity(), storyService.fetchStoriesIfNotExists())
            Log.e(TAG, "yeah")
            storyService.startListen()
            viewState.initUi()
            router.newRootScreen(Screen.WALL)
        }
    }

    private suspend fun requestLocation(isAfterRationale: Boolean = false) {
        val result =
            if (isAfterRationale) permissions.get().requestAfterRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
            else permissions.get().request(Manifest.permission.ACCESS_COARSE_LOCATION)
        return when (result) {
            is PermissionResult.Granted             -> Unit
            is PermissionResult.Denied              -> requestLocation()
            is PermissionResult.ShouldShowRationale -> {
                rationaleDialog.get().request("Россия охуенна, братан", "Да").await()
                requestLocation(true)
            }
            is PermissionResult.NeverAskAgain       -> {
                rationaleDialog.get().request("Поди ка ты лесом", "Пойти").await()
                appSettingsPage.get().requestSettingsPage().await()
                requestLocation()
            }
        }
    }

    private suspend fun requestAuth(): GoogleSignInAccount {
        val result = googleAuth.get().requestAuthDialog().await()
        return when (result) {
            is AuthResult.ResultGranted      -> result.signInAccount
            is AuthResult.ResultCanceled     -> {
                rationaleDialog.get().request("Попади пальцем по своему аккаунту", "Я постараюсь")
                        .await()
                requestAuth()
            }
            is AuthResult.ResultApiException -> {
                rationaleDialog.get().request("Ваня красавчек, а у тебя нет интернета", "Понятно")
                        .await()
                requestAuth()
            }
        }
    }

    fun wallClicked() = router.newRootScreen(Screen.WALL)

    fun profileClicked() = router.navigateTo(Screen.PROFILE)


    companion object {
        const val TAG = "MainActivityPresenter"
    }

}