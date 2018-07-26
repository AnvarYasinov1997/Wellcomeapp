@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

package com.wellcomeapp

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import kotlinx.coroutines.experimental.runBlocking
import java.util.logging.FileHandler
import java.util.logging.Logger

val fileHandler by lazy {
    FileHandler("Logs.txt").apply { formatter = JsonFormatter()}
}

private val logger by lazy {
    Logger.getLogger("main").apply { addHandler(fileHandler) }
}

val playServicesStream by lazy {
    WellcomeTimer::class.java.classLoader.getResourceAsStream("wellcomeapp-cc11e-30f446861838.json")
}

fun main(args: Array<String>) {
    println("!!!!")
    runBlocking {
        logger.info("init")
        init()
        val timer = WellcomeTimer(EventRemover())
        val actor = timer.timerActor()
        startObserve { actor.send(it) }.join()
    }
}

fun init() {
    val credentials = GoogleCredentials.fromStream(playServicesStream)
    logger.info("credentials $credentials")
    val options = FirebaseOptions.Builder().setCredentials(credentials).build()
    logger.info("options $options")
    FirebaseApp.initializeApp(options)
    logger.info("initialized")
}
