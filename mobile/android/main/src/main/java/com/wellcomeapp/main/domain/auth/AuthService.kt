@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

package com.wellcomeapp.main.domain.auth

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.wellcome.core.Cache
import com.wellcome.core.firebase.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import wellcome.common.core.CacheConst
import wellcome.common.core.FirebaseConstants
import wellcome.common.entity.CityData
import wellcome.common.entity.UserData
import wellcome.common.location.LocationService

interface AuthService {
    suspend fun signInWithGoogle(account: GoogleSignInAccount): Deferred<FirebaseUser>
    suspend fun bindToCity(): Job
    fun isAuthenticated(): Boolean
    fun bindUser(firebaseUser: FirebaseUser): Job
}

class GoogleAuthService(
        private val context: Context,
        private val cache: Cache,
        private val locationService: LocationService
) : AuthService {
    override suspend fun signInWithGoogle(account: GoogleSignInAccount) = async {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        FirebaseAuth.getInstance().signIn(credential).await()
    }

    override fun bindUser(firebaseUser: FirebaseUser): Job = launch {
        val user = FirebaseFunctions.getInstance()
                .getHttpsCallable("initUser")
                .request<UserData>().await()
        cacheUserData(user)
    }

    override suspend fun bindToCity() = launch {
        val latLon = locationService.getLastKnownLocation()
        val params = mapOf("lat" to latLon.lat, "lon" to latLon.lon)
        val city = FirebaseFunctions.getInstance()
                .getHttpsCallable("initCity")
                .request<CityData>(params).await()
        cacheUserCity(city)
    }

    override fun isAuthenticated(): Boolean =
            cache.getString(CacheConst.USER_REF, "").isNotEmpty()
                    && FirebaseAuth.getInstance().currentUser != null
                    && GoogleSignIn.getLastSignedInAccount(context) != null


    private fun cacheUserData(userData: UserData) {
        cache.cacheString(CacheConst.USER_REF, userData.ref)
        cache.cacheString(CacheConst.USER_ID, userData.id)
        cache.cacheString(CacheConst.USER_CITY, userData.cityName)
        cache.cacheString(CacheConst.USER_NAME, userData.displayedName)

        val photoUrl = userData.photoUrl
        if (photoUrl != null && photoUrl.isNotEmpty())
            cache.cacheString(CacheConst.USER_PHOTO, photoUrl)
    }

    private fun cacheUserCity(cityData: CityData){
        cache.cacheString(CacheConst.USER_CITY, cityData.name)
        cache.cacheString(CacheConst.USER_CITY_REF, cityData.ref)
    }
}