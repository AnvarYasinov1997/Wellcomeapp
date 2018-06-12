package com.wellcomeapp

import com.google.cloud.firestore.CollectionReference
import com.google.firebase.cloud.FirestoreClient

class EventRemover {

    fun remove(refPath: String){
        println("remove $refPath")
        FirestoreClient.getFirestore().document(refPath).delete()
        FirestoreClient.getFirestore().document(refPath).collections.forEach {
            it.deleteCollection()
        }
    }
}

private fun CollectionReference.deleteCollection(batchSize: Int = 100){
    try {
        val future = this@deleteCollection.limit(batchSize).get()
        var count = 0
        future.get().documents.forEach { doc ->
            doc.reference.delete()
            count++
        }
        if (count >= batchSize) this@deleteCollection.deleteCollection(batchSize)

    }catch (e: Exception) {
        println("error deleting subcollection ${e.message}")
    }
}