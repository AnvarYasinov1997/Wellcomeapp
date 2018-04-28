package com.mistreckless.support.wellcomeapp.data.auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.mistreckless.support.wellcomeapp.data.CacheData
import com.mistreckless.support.wellcomeapp.data.repository.LocationRepository
import com.mistreckless.support.wellcomeapp.data.rxfirebase.*
import wellcome.common.entity.UserData
import io.reactivex.*
import io.reactivex.functions.Consumer
import wellcome.common.entity.CityData

interface AuthService {
    fun signInWithGoogle(account: GoogleSignInAccount): Single<Unit>
    fun bindToCity(): Single<Unit>
    fun isAuthenticated(): Boolean
}

class GoogleAuthService(
    private val cacheData: CacheData,
    private val locationRepository: LocationRepository
) : AuthService {
    override fun signInWithGoogle(account: GoogleSignInAccount): Single<Unit> {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        return FirebaseAuth.getInstance().signIn(credential)
            .flatMap(this::confirmUser)
    }

    override fun bindToCity(): Single<Unit> = locationRepository.getCurrentCity()
        .flatMap(this::checkCity)

    override fun isAuthenticated() = cacheData.getString(CacheData.USER_REF).isNotEmpty()

    private fun confirmUser(firebaseUser: FirebaseUser) =
        FirebaseFirestore.getInstance().collection(FirebaseConstants.USER)
            .whereEqualTo(USER_ID, firebaseUser.uid)
            .getValues(UserData::class.java)
            .filter(List<UserData>::isNotEmpty)
            .flatMap { users -> Maybe.just(Unit).doOnSuccess { cacheUserData(users.first()) } }
            .switchIfEmpty(registryNewUser(firebaseUser))

    private fun registryNewUser(user: FirebaseUser): Single<Unit> {
        val userRef = FirebaseFirestore.getInstance().collection(FirebaseConstants.USER).document()
        val cityName = cacheData.getString(CacheData.USER_CITY)
        return checkCity(cityName)
            .map {
                UserData(
                    user.uid,
                    userRef.id,
                    cityName,
                    user.displayName ?: "Anonymous"
                )
            }
            .flatMap {
                userRef.setValue(it).doOnComplete { cacheUserData(it) }.toSingleDefault(Unit)
            }
    }

    private fun checkCity(cityName: String) =
        FirebaseFirestore.getInstance().collection(FirebaseConstants.CITY)
            .whereEqualTo(CITY_NAME, cityName)
            .getValues(CityData::class.java)
            .filter(List<CityData>::isNotEmpty)
            .flatMap { Maybe.just(Unit).doOnSuccess(cacheCity(it.first().ref)) }
            .switchIfEmpty(Maybe.just(Unit).flatMap { initCity(cityName) })
            .onErrorReturn { if (it is FirebaseFirestoreException) Unit }
            .toSingle()

    private fun initCity(cityName: String) = with(
        FirebaseFirestore.getInstance().collection(FirebaseConstants.CITY)
            .document()
    ) {
        setValue(CityData(id, cityName))
            .toSingleDefault(Unit)
            .doOnSuccess(cacheCity(id))
            .toMaybe()
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun cacheCity(cityRef: String): Consumer<Unit> =
        Consumer { cacheData.cacheString(CacheData.USER_CITY_REF, cityRef) }

    private fun cacheUserData(userData: UserData) {
        cacheData.cacheString(CacheData.USER_REF, userData.ref)
        cacheData.cacheString(CacheData.USER_ID, userData.id)
        cacheData.cacheString(CacheData.USER_CITY, userData.cityName)
        cacheData.cacheString(CacheData.USER_NAME, userData.displayedName)

        val photoUrl = userData.photoUrl
        if (photoUrl != null && photoUrl.isNotEmpty())
            cacheData.cacheString(CacheData.USER_PHOTO, photoUrl)
    }

    companion object {
        const val USER_ID = "id"
        const val CITY_NAME = "name"
    }
}
