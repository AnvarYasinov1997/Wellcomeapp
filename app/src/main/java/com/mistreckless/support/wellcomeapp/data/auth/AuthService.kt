package com.mistreckless.support.wellcomeapp.data.auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.mistreckless.support.wellcomeapp.data.CacheData
import com.mistreckless.support.wellcomeapp.data.repository.LocationRepository
import com.mistreckless.support.wellcomeapp.data.rxfirebase.FirebaseConstants
import com.mistreckless.support.wellcomeapp.data.rxfirebase.getValues
import com.mistreckless.support.wellcomeapp.data.rxfirebase.setValue
import com.mistreckless.support.wellcomeapp.data.rxfirebase.signIn
import com.mistreckless.support.wellcomeapp.domain.entity.CityData
import com.mistreckless.support.wellcomeapp.domain.entity.UserData
import com.mistreckless.support.wellcomeapp.ui.value
import io.reactivex.*

interface AuthService {
    fun signInWithGoogle(account: GoogleSignInAccount): Single<Unit>

    fun bindToCity(): Single<Unit>
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

    private fun confirmUser(firebaseUser: FirebaseUser) = isUserAlreadyRegistered(firebaseUser.uid)
        .filter(Boolean::value)
        .map { Unit }
        .switchIfEmpty(registryNewUser(firebaseUser))

    private fun isUserAlreadyRegistered(uId: String): Single<Boolean> =
        FirebaseFirestore.getInstance().collection(FirebaseConstants.USER)
            .whereEqualTo(USER_ID, uId)
            .getValues(UserData::class.java)
            .map(List<UserData>::isNotEmpty)

    private fun registryNewUser(user: FirebaseUser): Single<Unit> {
        val userRef = FirebaseFirestore.getInstance().collection(FirebaseConstants.USER).document()
        val cityName = cacheData.getString(CacheData.USER_CITY)
        return checkCity(cityName)
            .map { UserData(user.uid, userRef.id, cityName, user.displayName ?: "Anonymous") }
            .flatMap { userRef.setValue(it).toSingleDefault(cacheUserData(it)) }
    }

    private fun checkCity(cityName: String) =
        FirebaseFirestore.getInstance().collection(FirebaseConstants.CITY)
            .whereEqualTo(CITY_NAME, cityName)
            .getValues(CityData::class.java)
            .filter(List<CityData>::isNotEmpty)
            .flatMap { cities -> Maybe.just(Unit)
                .doOnSuccess { cacheData.cacheString(CacheData.USER_CITY_REF, cities.first().ref) }
            }
            .switchIfEmpty(initCity(cityName))
            .onErrorReturn { if (it is FirebaseFirestoreException) Unit }
            .toSingle()

    private fun initCity(cityName: String) = with(
        FirebaseFirestore.getInstance().collection(FirebaseConstants.CITY)
            .document()
    ) {
        setValue(CityData(id, cityName))
            .doOnComplete { cacheData.cacheString(CacheData.USER_CITY_REF, id) }
            .toSingleDefault(Unit)
            .toMaybe()
    }


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