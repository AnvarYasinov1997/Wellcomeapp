package com.wellcome.rest

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.gson.GsonConverter
import io.ktor.http.ContentType
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun main(args: Array<String>){
    initFirebaseApp(LoggerFactory.getLogger("main"))
    embeddedServer(Netty, commandLineEnvironment(args)).start()
}

fun Application.rest(){
    install(TokenVerification)
    install(CallLogging)
    install(ContentNegotiation){
        register(ContentType.Application.Json, GsonConverter())
    }
    routing {
        get("/"){
            log.info("work!!")
        }
    }
}

fun initFirebaseApp(log: Logger) {
    val databaseUrl = "https://wellcomeapp-cc11e.firebaseio.com"
    val serviceAccount = "wellcomeapp-cc11e-firebase-adminsdk-qoxy4-310b8e400c.json"
    val options = FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.fromStream(Application::class.java.classLoader.getResourceAsStream(
            serviceAccount)))
        .setDatabaseUrl(databaseUrl)
        .build()
    FirebaseApp.initializeApp(options)
    log.info("firebase initialized")
}