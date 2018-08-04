package com.wellcome.configuration

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.maps.GeoApiContext
import io.ktor.application.Application
import org.slf4j.Logger
import java.util.*

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

fun initGoogleMaps(log: Logger): GeoApiContext {
    val props = Properties()
    props.load(Application::class.java.classLoader.getResourceAsStream("wellcome.property"))
    val key = props.getProperty("googleMapsKey")
    val context = GeoApiContext.Builder().apiKey(key).build()
    log.info("google maps initialized")
    return context
}