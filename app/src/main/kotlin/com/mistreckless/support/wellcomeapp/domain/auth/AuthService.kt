@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

package com.mistreckless.support.wellcomeapp.domain.auth

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.wellcome.utils.firebase.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import wellcome.common.cache.Cache
import wellcome.common.cache.CacheConst
import wellcome.common.entity.CityData
import wellcome.common.entity.UserData
import wellcome.common.location.LocationService

interface AuthService {
    suspend fun signInWithGoogle(account: GoogleSignInAccount): Deferred<FirebaseUser>
    suspend fun checkUserExist(firebaseUser: FirebaseUser): Deferred<Boolean>
    suspend fun bindToCity(): Job
    suspend fun registryNewUser(firebaseUser: FirebaseUser) : Job
    fun isAuthenticated(): Boolean
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

    override suspend fun checkUserExist(firebaseUser: FirebaseUser): Deferred<Boolean> = async {
        val users = FirebaseFirestore.getInstance().collection(FirebaseConstants.USER)
            .whereEqualTo(UserData.ID, firebaseUser.uid)
            .getValues(UserData::class.java).await()
        if (users.isNotEmpty()) cacheUserData(users[0])
        users.isNotEmpty()
    }

    override suspend fun bindToCity() = launch {
        val cityName = locationService.getCurrentCity()
        val collection = FirebaseFirestore.getInstance().collection(FirebaseConstants.CITY)
        val cities = collection.whereEqualTo(CityData.NAME, cityName)
            .getValues(CityData::class.java).await()
        if (cities.isNotEmpty()) cache.cacheString(CacheConst.USER_CITY_REF, cities[0].ref)
        else with(collection.document()) {
            setValue(CityData(id, cityName)).join()
            cache.cacheString(CacheConst.USER_CITY_REF,id)
        }
        updateUserCity(cityName)
    }

    override fun isAuthenticated(): Boolean =
        cache.getString(CacheConst.USER_REF, "").isNotEmpty()
                && FirebaseAuth.getInstance().currentUser != null
                && GoogleSignIn.getLastSignedInAccount(context) != null

    override suspend fun registryNewUser(firebaseUser: FirebaseUser) = launch {
        val userRef = FirebaseFirestore.getInstance().collection(FirebaseConstants.USER).document()
        val cityName = cache.getString(CacheConst.USER_CITY, "")
        val user = UserData(
            firebaseUser.uid,
            userRef.id,
            cityName,
            firebaseUser.displayName ?: "Anonymous"
        )
        userRef.setValue(user).join()
        cacheUserData(user)
    }

    private suspend fun updateUserCity(cityName: String)  {
        val userRef = cache.getString(CacheConst.USER_REF,"")
        val document = FirebaseFirestore.getInstance().collection(FirebaseConstants.USER).document(userRef)
        val user = document.getValue(UserData::class.java).await()
        if (user.cityName != cityName) document.updateFields(mapOf(UserData.CITY_NAME to cityName))
    }

    private fun cacheUserData(userData: UserData) {
        cache.cacheString(CacheConst.USER_REF, userData.ref)
        cache.cacheString(CacheConst.USER_ID, userData.id)
        cache.cacheString(CacheConst.USER_CITY, userData.cityName)
        cache.cacheString(CacheConst.USER_NAME, userData.displayedName)

        val photoUrl = userData.photoUrl
        if (photoUrl != null && photoUrl.isNotEmpty())
            cache.cacheString(CacheConst.USER_PHOTO, photoUrl)
    }

}