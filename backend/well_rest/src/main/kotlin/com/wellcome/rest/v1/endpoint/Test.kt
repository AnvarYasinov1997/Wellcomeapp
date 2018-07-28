package com.wellcome.rest.v1.endpoint

import com.wellcome.rest.property.SenderProperty
import com.wellcome.rest.security.TokenVerification
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.gson.GsonConverter
import io.ktor.http.ContentType
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import org.koin.ktor.ext.inject

fun Application.rest() {
    val authSender by inject<SenderProperty>("auth")
//    install(TokenVerification)
    install(CallLogging)
    install(ContentNegotiation) {
        register(ContentType.Application.Json, GsonConverter())
    }
    routing {
        get("/") {
            val firebaseToken = call.attributes[TokenVerification.firebaseTokenKey]
            log.info("work!!")
            call.respond(authSender)
        }
    }
}