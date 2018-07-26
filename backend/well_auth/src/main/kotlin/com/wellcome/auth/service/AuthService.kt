package com.wellcome.auth.service

import com.google.firebase.auth.FirebaseToken
import com.wellcome.auth.repository.AuthRepository
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import org.slf4j.Logger
import wellcome.common.mpp.entity.CityData
import wellcome.common.mpp.entity.LatLon
import wellcome.common.mpp.entity.UserData


interface AuthService {
    fun initUser(firebaseToken: FirebaseToken): Deferred<UserData>
    fun initCity(firebaseToken: FirebaseToken, latLon: LatLon): Deferred<CityData>
}

class AuthServiceImpl(private val logger: Logger, private val authRepository: AuthRepository) : AuthService {

    override fun initUser(firebaseToken: FirebaseToken): Deferred<UserData> = async {
        logger.info("initUser called $firebaseToken")
        val user = authRepository.findUserByUid(firebaseToken.uid)
        return@async user ?: authRepository
            .saveNewUser(firebaseToken.uid, firebaseToken.name ?: "Anonymous")
            .await()
    }

    override fun initCity(firebaseToken: FirebaseToken, latLon: LatLon): Deferred<CityData> = async {
        logger.info("initCity called $latLon $firebaseToken")
        val locality = authRepository.getLocality(latLon).await()
        val city = authRepository.findCityByName(locality)
        val user = authRepository.findUserByUid(firebaseToken.uid)!!

        if (city != null) {
            authRepository.updateUserCityName(user.ref, locality).join()
            return@async city!!
        }

        return@async with(authRepository) {
            val timeZoneId = getTimeZoneId(latLon).await()
            return@with batchNewUserCity(locality, timeZoneId, user.ref).await()
        }
    }
}