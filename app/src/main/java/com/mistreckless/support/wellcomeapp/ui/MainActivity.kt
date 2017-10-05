package com.mistreckless.support.wellcomeapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.widget.Toolbar
import android.view.View
import butterknife.BindView
import com.google.android.gms.common.api.GoogleApiClient
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.domain.entity.NewUserState
import com.mistreckless.support.wellcomeapp.ui.screen.Layout
import com.mistreckless.support.wellcomeapp.ui.screen.camera.CameraActivity
import com.mistreckless.support.wellcomeapp.ui.screen.drawer.Drawer
import com.mistreckless.support.wellcomeapp.ui.screen.profile.Profile
import com.mistreckless.support.wellcomeapp.ui.screen.registry.Registry
import com.mistreckless.support.wellcomeapp.ui.screen.wall.Wall
import com.mxn.soul.flowingdrawer_core.ElasticDrawer
import com.mxn.soul.flowingdrawer_core.FlowingDrawer
import com.mxn.soul.flowingdrawer_core.FlowingMenuLayout

@Layout(id = R.layout.activity_main)
class MainActivity : BaseActivity<MainActivityPresenter,MainActivityPresenterProviderFactory>(), MainActivityView, MainActivityRouter {
    @BindView(R.id.drawer_layout)
    lateinit var drawerLayout: FlowingDrawer
    @BindView(R.id.menu_layout)
    lateinit var menuLayout: FlowingMenuLayout

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.authResult(requestCode, resultCode, data)
    }

    @SuppressLint("RestrictedApi", "PrivateResource")
    override fun setToolbar(toolbar: Toolbar?, isAddedToBackStack: Boolean) {
        setSupportActionBar(toolbar)
        when (isAddedToBackStack) {
            true -> {
                toolbar?.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
                toolbar?.setNavigationOnClickListener { onBackPressed() }
                drawerLayout.setTouchMode(ElasticDrawer.TOUCH_MODE_NONE)
            }
            false -> {
                toolbar?.setNavigationIcon(R.drawable.ic_menu_white_24dp)
                toolbar?.setNavigationOnClickListener { drawerLayout.openMenu(true) }
                drawerLayout.setTouchMode(ElasticDrawer.TOUCH_MODE_FULLSCREEN)
                if (menuLayout.visibility == View.GONE) {
                    menuLayout.visibility = View.VISIBLE
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.menu_container, Drawer())
                            .commit()
                }
            }
        }

    }

    override fun onBackPressed() {
        if (drawerLayout.isMenuVisible)
            drawerLayout.closeMenu(true)
        else
            super.onBackPressed()
    }

    override fun onDestroy() {
        presenter.destroyed()
        super.onDestroy()
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
        const val RC_SIGN_IN = 88
    }
}

interface MainActivityView : BaseActivityView

interface MainActivityRouter {
    fun navigateToRegistry(newUserState: NewUserState)
    fun navigateToGoogleAuthDialog(googleApiClient: GoogleApiClient)
    fun navigateToWall()
    fun navigateToProfile()
    fun navigateToCamera()
}