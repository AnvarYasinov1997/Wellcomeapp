@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

package com.wellcomeapp

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import kotlinx.coroutines.experimental.runBlocking
import java.io.File
import java.io.FileInputStream

fun main(args: Array<String>) = runBlocking<Unit> {
    println("Hello")
    init()
    val actor = timerActor()
    startObserve { actor.send(it) }.join()
}

val playServicesPath = "${File(System.getProperty("user.dir")).parent}/wellcomeapp-cc11e-30f446861838.json"

fun init() {
    val account = FileInputStream(playServicesPath)
    val credentials = GoogleCredentials.fromStream(account)
    val options = FirebaseOptions.Builder()
            .setCredentials(credentials)
            .build()
    FirebaseApp.initializeApp(options)

}