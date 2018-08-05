package com.wellcome.main.repository

import com.google.firebase.cloud.FirestoreClient
import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi
import com.google.maps.TimeZoneApi
import com.google.maps.model.AddressComponentType
import com.google.maps.model.AddressType
import com.google.maps.model.LatLng
import com.wellcome.main.commitOperations
import com.wellcome.main.getValues
import com.wellcome.main.setValue
import com.wellcome.main.updateFields
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import org.slf4j.Logger
import wellcome.common.mpp.core.FirebaseConstants
import wellcome.common.mpp.entity.CityData
import wellcome.common.mpp.entity.LatLon
import wellcome.common.mpp.entity.UserData

interface AuthRepository {

    fun getLocality(latLon: LatLon): Deferred<String>
    fun getTimeZoneId(latLon: LatLon): Deferred<String>
    suspend fun findUserByUid(uid: String): UserData?
    suspend fun findCityByName(name: String): CityData?
    fun saveNewUser(uid: String, name: String): Deferred<UserData>
    fun batchNewUserCity(cityName: String, zoneId: String, userRef: String): Deferred<CityData>
    fun updateUserCityName(userRef: String, cityName: String): Job
}

class AuthRepositoryImpl(private val logger: Logger, private val geoContext: GeoApiContext) : AuthRepository {
    private val db by lazy {
        FirestoreClient.getFirestore()
    }

    override fun saveNewUser(uid: String, name: String): Deferred<UserData> = async {
        val userRef = db.collection(FirebaseConstants.USER).document()
        val newUser = UserData(uid, userRef.id, displayedName = name)
        userRef.setValue(newUser).join()
        return@async newUser
    }

    override fun batchNewUserCity(cityName: String, zoneId: String, userRef: String): Deferred<CityData> = async {
        val batch = db.batch()

        val cityRef = db.collection(FirebaseConstants.CITY).document()
        val newCity = CityData(cityRef.id, cityName, zoneId)
        batch.set(cityRef, newCity)

        val userDoc = db.collection(FirebaseConstants.USER).document(userRef)
        batch.update(userDoc, mapOf(UserData.CITY_NAME to cityName))

        batch.commitOperations().join()

        return@async newCity
    }

    override fun updateUserCityName(userRef: String, cityName: String): Job =
        db.collection(FirebaseConstants.USER).document(userRef).updateFields(mapOf(UserData.CITY_NAME to cityName))


    override fun getLocality(latLon: LatLon): Deferred<String> = async {
        val results = GeocodingApi
            .reverseGeocode(geoContext, LatLng(latLon.lat, latLon.lon))
            .resultType(AddressType.LOCALITY)
            .await()

        results.forEach { res ->
            logger.info("res $res")
            res.addressComponents.forEach { addressComponent ->
                logger.info("address $addressComponent")
                addressComponent.types.forEach { type ->
                    logger.info("type $type")
                    if (type == AddressComponentType.LOCALITY) {
                        logger.info("$type $addressComponent")
                        return@async addressComponent.longName
                    }
                }
            }
        }
        throw Exception("Cannot determinate city with $latLon")
    }

    override fun getTimeZoneId(latLon: LatLon): Deferred<String> = async {
        val result = TimeZoneApi.getTimeZone(geoContext, LatLng(latLon.lat, latLon.lon)).await()
        logger.info("zoneID ${result.id}")
        return@async result.id
    }

    override suspend fun findUserByUid(uid: String): UserData? = db
        .collection(FirebaseConstants.USER)
        .whereEqualTo(UserData.ID, uid)
        .limit(1)
        .getValues(UserData::class.java).await()
        .firstOrNull()

    override suspend fun findCityByName(name: String): CityData? = db
        .collection(FirebaseConstants.CITY)
        .whereEqualTo(CityData.NAME, name)
        .limit(1)
        .getValues(CityData::class.java).await()
        .firstOrNull()
}