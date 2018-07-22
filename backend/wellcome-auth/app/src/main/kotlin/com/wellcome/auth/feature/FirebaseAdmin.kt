package com.wellcome.auth.feature

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.application.*
import io.ktor.util.AttributeKey

class FirebaseAdmin(val configuration: Configuration) {

    class Configuration {
        var databaseUrl: String = "https://wellcomeapp-cc11e.firebaseio.com"
        var serviceAccount: String = "wellcomeapp-cc11e-firebase-adminsdk-qoxy4-310b8e400c.json"
    }

    companion object Feature : ApplicationFeature<ApplicationCallPipeline, Configuration, FirebaseAdmin> {
        override val key: AttributeKey<FirebaseAdmin> = AttributeKey("FirebaseAdmin")

        override fun install(pipeline: ApplicationCallPipeline, configure: Configuration.() -> Unit): FirebaseAdmin {
            val configuration = FirebaseAdmin.Configuration().apply(configure)
            val firebaseAdmin = FirebaseAdmin(configuration)

            pipeline.intercept(ApplicationCallPipeline.Infrastructure) {
                application.log.info("Conf ${configuration.databaseUrl} ${configuration.serviceAccount}")
                firebaseAdmin.init()
                proceed()
            }
            return firebaseAdmin
        }
    }

    private fun init() {
        val options = FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(Application::class.java.classLoader.getResourceAsStream(configuration.serviceAccount)))
            .setDatabaseUrl(configuration.databaseUrl)
            .build()
        FirebaseApp.initializeApp(options)
    }
}