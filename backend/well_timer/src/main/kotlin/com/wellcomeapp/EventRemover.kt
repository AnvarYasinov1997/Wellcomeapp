package com.wellcomeapp

import com.google.cloud.firestore.CollectionReference
import com.google.firebase.cloud.FirestoreClient
import java.util.logging.Level
import java.util.logging.Logger

private val logger by lazy {
    Logger.getLogger("EventRemover").apply { addHandler(fileHandler) }
}

class EventRemover {

    fun remove(refPath: String) {
        logger.info("remove $refPath")
        FirestoreClient.getFirestore().document(refPath).delete()
        FirestoreClient.getFirestore().document(refPath).collections.forEach {
            it.deleteCollection()
        }

    }
}

private fun CollectionReference.deleteCollection(batchSize: Int = 100) {
    try {
        val future = this@deleteCollection.limit(batchSize).get()
        var count = 0
        future.get().documents.forEach { doc ->
            doc.reference.delete()
            count++
        }
        if (count >= batchSize) this@deleteCollection.deleteCollection(batchSize)

    } catch (e: Exception) {
        logger.severe("error deleting subcollection ${e.message}")
    }
}