package com.wellcome.auth

import com.wellcome.auth.feature.TokenVerification
import com.wellcome.auth.service.AuthService
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.gson.GsonConverter
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import kotlinx.coroutines.experimental.launch
import org.koin.ktor.ext.inject
import org.koin.standalone.StandAloneContext.startKoin
import wellcome.common.mpp.entity.LatLon
import wellcome.common.mpp.entity.UserData

fun Application.auth() {
    log.error("MAIN1")
    initFirebaseApp(log)
    val geoContext = initGoogleMaps(log)
    startKoin(listOf(authModule(log, geoContext)))
    install(TokenVerification)
    install(CallLogging)
    install(ContentNegotiation){
        register(ContentType.Application.Json, GsonConverter())
    }

    val authService: AuthService by inject()

    routing {
        get("/auth/initUser"){
            log.info("initUser")
            val firebaseToken = call.attributes[TokenVerification.firebaseTokenKey]
            val user = authService.initUser(firebaseToken).await()
            call.respond(HttpStatusCode.OK, user)
        }
        get("auth/initCity"){
            log.info("initCity")
            val lat = call.request.queryParameters["lat"]
            val lon = call.request.queryParameters["lon"]
            if (lat?.toDoubleOrNull() == null && lon?.toDoubleOrNull() == null){
                call.response.status(HttpStatusCode.BadRequest)
                finish()
            }

            val firebaseToken = call.attributes[TokenVerification.firebaseTokenKey]
            val city = authService.initCity(firebaseToken, LatLon(lat!!.toDouble(), lon!!.toDouble())).await()
            call.respond(city)
        }
    }
}

fun Application.main2(){
    log.error("MAIN2")
}