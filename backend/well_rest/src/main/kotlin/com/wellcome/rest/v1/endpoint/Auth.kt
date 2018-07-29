package com.wellcome.rest.v1.endpoint

import com.wellcome.rest.handlers.InitCityAuthHandler
import com.wellcome.rest.handlers.InitUserAuthHandler
import com.wellcome.rest.security.TokenVerification
import com.wellcome.rest.utils.PathsV1
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.gson.GsonConverter
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.routing.get
import io.ktor.routing.routing
import org.koin.ktor.ext.inject

fun Application.auth() {
    install(CallLogging)
    install(TokenVerification)
    install(ContentNegotiation) {
        register(ContentType.Application.Json, GsonConverter())
    }

    routing {
        get(PathsV1.INIT_USER) {
            val authHandler by inject<InitUserAuthHandler>()
            authHandler.handle(call.attributes[TokenVerification.firebaseTokenKey])
        }
        get(PathsV1.INIT_SITY) {
            val authHandler by inject<InitCityAuthHandler>()
            val lat = call.request.queryParameters["lat"]
            val lon = call.request.queryParameters["lon"]
            if (lat?.toDoubleOrNull() == null && lon?.toDoubleOrNull() == null) {
                call.response.status(HttpStatusCode.BadRequest)
                finish()
            }
            authHandler.handle(call.attributes[TokenVerification.firebaseTokenKey], lat!!.toDouble(), lon!!.toDouble())
        }
    }
}