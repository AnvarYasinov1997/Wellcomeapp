package js.externals.firebase.admin.FirebaseFirestore

import js.externals.firebase.admin.database.Database
import js.externals.firebase.admin.storage.Storage

external interface DocumentData {
}
@Suppress("NOTHING_TO_INLINE")
inline operator fun DocumentData.get(field: String): Any? = asDynamic()[field]

@Suppress("NOTHING_TO_INLINE")
inline operator fun DocumentData.set(field: String, value: Any) {
    if (value is Long) asDynamic()[field] = value.toInt()
    else asDynamic()[field] = value
}

@JsModule("firebase-admin")
external object Admin {
    val credential: dynamic

    fun initializeApp(config: FirebaseAppOptions)
    fun database(): Database
    fun firestore(): Firestore
    fun storage(): Storage
}

data class FirebaseAppOptions(
    val credential: dynamic = undefined,
    val databaseAuthVariableOverride: Any? = undefined,
    val databaseURL: String? = undefined,
    val storageBucket: String? = undefined,
    val projectId: String? = undefined
)