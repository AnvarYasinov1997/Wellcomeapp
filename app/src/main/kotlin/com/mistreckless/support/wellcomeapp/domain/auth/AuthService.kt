@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

package com.mistreckless.support.wellcomeapp.domain.auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import io.reactivex.Single
import kotlinx.coroutines.experimental.Job
import wellcome.common.cache.Cache
import wellcome.common.cache.CacheConst
import wellcome.common.location.LocationService

interface AuthService {
    suspend fun signInWithGoogle(account: GoogleSignInAccount)
    suspend fun bindToCity()
    fun isAuthenticated(): Boolean
}

class GoogleAuthService(
    private val cache: Cache,
    private val locationService: LocationService
) : AuthService {
    override suspend fun signInWithGoogle(account: GoogleSignInAccount) {

    }

    override suspend fun bindToCity() {
        val city = locationService.getCurrentCity()

    }

    override fun isAuthenticated(): Boolean  = cache.getString(CacheConst.USER_REF,"").isNotEmpty()

}