@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

package com.wellcomeapp

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import kotlinx.coroutines.experimental.runBlocking
import java.io.File
import java.io.FileInputStream
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import javax.servlet.annotation.WebListener

//for local run
fun main(args: Array<String>) = runBlocking {
    init()
    val timer = WellcomeTimer(EventRemover())
    val actor = timer.timerActor()
    startObserve { actor.send(it) }.join()
}

val playServicesStream = WellcomeInitializer::class.java.classLoader.getResourceAsStream("wellcomeapp-cc11e-30f446861838.json")/*"${File(System.getProperty("user.dir")).parent}/wellcomeapp-cc11e-30f446861838.json"*/

fun init() {
    val credentials = GoogleCredentials.fromStream(playServicesStream)
    val options = FirebaseOptions.Builder()
            .setCredentials(credentials)
            .build()
    FirebaseApp.initializeApp(options)

}

@WebListener
class WellcomeInitializer : ServletContextListener{
    override fun contextInitialized(sce: ServletContextEvent?) = runBlocking {
        init()
        val timer = WellcomeTimer(EventRemover())
        val actor = timer.timerActor()
        startObserve { actor.send(it) }.join()
    }

    override fun contextDestroyed(sce: ServletContextEvent?) {
    }

}

