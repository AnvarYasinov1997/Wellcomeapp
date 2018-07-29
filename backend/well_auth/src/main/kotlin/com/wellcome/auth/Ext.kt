package com.wellcome.auth

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.Query
import com.google.cloud.firestore.WriteBatch
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.maps.GeoApiContext
import io.ktor.application.Application
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
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

fun <T : Any> Query.getValues(clazz: Class<T>): Deferred<List<T>> = async {
    val snapshot = this@getValues.get().get()
    if (snapshot.isEmpty) emptyList<T>()
    else snapshot.toObjects(clazz)
}

fun <T : Any> DocumentReference.setValue(value: T): Job = launch {
    this@setValue.set(value).get()
}

fun DocumentReference.updateFields(fields: Map<String, Any>): Job = launch {
    this@updateFields.update(fields).get()
}

fun WriteBatch.commitOperations(): Job = launch {
    this@commitOperations.commit().get()
}