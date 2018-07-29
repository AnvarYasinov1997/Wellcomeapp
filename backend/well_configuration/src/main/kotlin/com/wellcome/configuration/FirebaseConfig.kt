package com.wellcome.configuration

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.application.Application
import org.slf4j.Logger

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