package com.mistreckless.support.wellcomeapp.ui

import android.content.Intent
import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.gms.common.api.GoogleApiClient
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.domain.entity.NewUserState
import com.mistreckless.support.wellcomeapp.ui.screen.Layout
import com.mistreckless.support.wellcomeapp.ui.screen.camera.CameraActivity
import com.mistreckless.support.wellcomeapp.ui.screen.profile.Profile
import com.mistreckless.support.wellcomeapp.ui.screen.registry.Registry
import com.mistreckless.support.wellcomeapp.ui.screen.wall.Wall
import kotlinx.android.synthetic.main.activity_main.*

@Layout(id = R.layout.activity_main, containerId =  R.id.container)
class MainActivity : BaseActivity<MainActivityPresenter>(), MainActivityView, MainActivityRouter {

    @InjectPresenter
    override lateinit var presenter: MainActivityPresenter

    @ProvidePresenter
    fun providePresenter() = presenterProvider.get()


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.authResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        if (drawerLayout.isMenuVisible)
            drawerLayout.closeMenu(true)
        else
            super.onBackPressed()
    }

    override fun navigateToRegistry(newUserState: NewUserState) {
        val registry = Registry.newInstance(newUserState)
        supportFragmentManager.beginTransaction()
                .add(R.id.container, registry, Registry.TAG)
                .commitNow()
    }

    override fun navigateToWall() {
        drawerLayout.closeMenu()
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, Wall(), Wall.TAG)
                .commitNow()
    }

    override fun navigateToProfile() {
        drawerLayout.closeMenu()
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, Profile(), Profile.TAG)
                .commitNow()
    }

    override fun navigateToCamera() {
        startActivity(Intent(this, CameraActivity::class.java))
    }

    override fun navigateToGoogleAuthDialog(googleApiClient: GoogleApiClient) {
        val intent = com.google.android.gms.auth.api.Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        startActivityForResult(intent, RC_SIGN_IN)
    }

    companion object {
        const val TAG = "MainActivity"
    }
}

interface MainActivityView : BaseActivityView

interface MainActivityRouter : BaseRouter {
    fun navigateToRegistry(newUserState: NewUserState)
    fun navigateToGoogleAuthDialog(googleApiClient: GoogleApiClient)
    fun navigateToWall()
    fun navigateToProfile()
    fun navigateToCamera()
}