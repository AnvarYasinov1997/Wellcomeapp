package com.wellcome.utils.firebase

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.SnapshotMetadata

object FirebaseConstants {
    const val USER = "user"
    const val CITY = "city"
    const val EVENT = "event"
    const val LIKE = "like"
}
sealed class DocumentState<T: Any>
class DocumentAdded<T: Any>(val data: T, val metadata: SnapshotMetadata, val ref: DocumentReference): DocumentState<T>()
class DocumentModified<T: Any>(val data: T, val metadata: SnapshotMetadata, val ref: DocumentReference) : DocumentState<T>()
class DocumentError<T: Any>(val exception: Exception, val ref: DocumentReference): DocumentState<T>()
class DocumentRemoved<T: Any>(val ref: DocumentReference): DocumentState<T>()

sealed class UploadState
data class ProgressState(val progress: Int = 0): UploadState()
data class UploadedState(val url: String): UploadState()
data class UploadError(val exception: Exception): UploadState()