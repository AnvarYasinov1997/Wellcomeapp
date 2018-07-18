package com.wellcome.auth.service

import com.google.firebase.auth.FirebaseToken
import com.google.firebase.cloud.FirestoreClient
import com.wellcome.auth.repository.AuthRepository
import com.wellcome.auth.setValue
import com.wellcome.auth.updateFields
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.joinAll
import org.slf4j.Logger
import wellcome.common.core.FirebaseConstants
import wellcome.common.entity.CityData
import wellcome.common.entity.LatLon
import wellcome.common.entity.UserData


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