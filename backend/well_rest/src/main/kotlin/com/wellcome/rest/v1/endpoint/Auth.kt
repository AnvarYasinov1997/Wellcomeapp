package com.wellcome.rest.v1.endpoint

import com.wellcome.rest.handlers.AuthHandler
import com.wellcome.rest.security.TokenVerification
import com.wellcome.rest.utils.PathsV1
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.gson.GsonConverter
import io.ktor.http.ContentType
import io.ktor.routing.get
import io.ktor.routing.routing
import org.koin.ktor.ext.inject

fun Application.auth() {
    install(CallLogging)
    install(ContentNegotiation) {
        register(ContentType.Application.Json, GsonConverter())
    }
    routing {
        get(PathsV1.INIT_USER) {
            val authHandler by inject<AuthHandler>()

            authHandler.handle(call.attributes[TokenVerification.firebaseTokenKey])
        }
        get(PathsV1.INIT_SITY) {
            val firebaseToken = call.attributes[TokenVerification.firebaseTokenKey]
        }
    }
}