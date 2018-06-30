package com.wellcome.core.firebase

import android.util.Log
import com.google.firebase.functions.HttpsCallableReference
import com.google.gson.Gson
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlin.coroutines.experimental.suspendCoroutine

inline fun <reified T>HttpsCallableReference.request(map: Map<String, Any> = mapOf()): Deferred<T> = async{
    suspendCoroutine<T> { cont ->
        this@request.call(map)
                .addOnSuccessListener { result ->
                    Log.e("functions", "success ${result.data}")
                    val gson = Gson()
                    val data = gson.fromJson<T>(gson.toJson(result.data), T::class.java)
                    cont.resume(data)
                }.addOnFailureListener {
                    cont.resumeWithException(it)
                }
    }
}