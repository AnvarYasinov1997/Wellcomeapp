@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

package com.wellcome.main.domain.auth

import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.wellcome.utils.firebase.getFirebaseToken
import com.wellcome.utils.firebase.signIn
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import wellcome.common.core.Cache
import wellcome.common.core.service.ApiService
import wellcome.common.core.service.LocationService
import wellcome.common.mpp.core.CacheConst
import wellcome.common.mpp.entity.CityData
import wellcome.common.mpp.entity.UserData

interface AuthService {
    suspend fun signInWithGoogle(account: GoogleSignInAccount): Deferred<FirebaseUser>
    suspend fun bindToCity(): Job
    fun bindUser(firebaseUser: FirebaseUser): Job
}

class GoogleAuthService(private val cache: Cache,
                        private val locationService: LocationService,
                        private val apiService: ApiService) : AuthService {

    override suspend fun signInWithGoogle(account: GoogleSignInAccount) = async {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        FirebaseAuth.getInstance().signIn(credential).await()
    }

    override fun bindUser(firebaseUser: FirebaseUser): Job = launch {
        val token = firebaseUser.getFirebaseToken().await()
        Log.e("token",
            "${token.token} ${token.authTimestamp} ${token.expirationTimestamp} ${token.issuedAtTimestamp} ${token.signInProvider} ${token.claims}")
        cache.cacheString(CacheConst.FIREBASE_TOKEN, token.token!!)

        val user = apiService.initUser().await()
        Log.e("user", "$user")
        cacheUserData(user)
    }

    override suspend fun bindToCity() = launch {
        val latLon = locationService.getLastKnownLocation().await()
        val city = apiService.initCity(latLon.lat, latLon.lon).await()
        cacheUserCity(city)
    }


    private fun cacheUserData(userData: UserData) {
        cache.cacheString(CacheConst.USER_REF, userData.ref)
        cache.cacheString(CacheConst.USER_ID, userData.id)
        cache.cacheString(CacheConst.USER_CITY, userData.cityName)
        cache.cacheString(CacheConst.USER_NAME, userData.displayedName)

        val photoUrl = userData.photoUrl
        if (photoUrl != null && photoUrl.isNotEmpty()) cache.cacheString(CacheConst.USER_PHOTO,
            photoUrl)
    }

    private fun cacheUserCity(cityData: CityData) {
        cache.cacheString(CacheConst.USER_CITY, cityData.name)
        cache.cacheString(CacheConst.USER_CITY_REF, cityData.ref)
    }
}