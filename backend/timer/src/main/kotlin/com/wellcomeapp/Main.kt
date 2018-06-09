@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

package com.wellcomeapp

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import kotlinx.coroutines.experimental.runBlocking
import java.io.FileInputStream

fun main(args: Array<String>) = runBlocking {
    init()
    val db = FirestoreClient.getFirestore()
    val future = db.collection("city").get()
    future.get().documents.forEach {
        println(it.toObject(City::class.java))
    }
}

data class City(val name: String = "", val ref: String = "" )

val playServicesPath = "${System.getProperty("user.dir")}/wellcomeapp-cc11e-30f446861838.json"

fun init() {
    val account = FileInputStream(playServicesPath)
    val credentials = GoogleCredentials.fromStream(account)
    val options = FirebaseOptions.Builder()
            .setCredentials(credentials)
            .build()
    FirebaseApp.initializeApp(options)

}