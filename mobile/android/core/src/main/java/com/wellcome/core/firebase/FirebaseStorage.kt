package com.wellcome.core.firebase

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce
import java.io.InputStream
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.coroutines.experimental.coroutineContext

fun StorageReference.uploadFileViaStream(
    stream: InputStream,
    coroutineContext: CoroutineContext,
    job: Job
): Deferred<String> = async(coroutineContext, parent = job) {
    val uploadTask = this@uploadFileViaStream.putStream(stream)
    suspendCancellableCoroutine<String> { cont ->
        val listener = OnCompleteListener<UploadTask.TaskSnapshot> { task ->
            stream.close()
            if (task.isSuccessful) cont.resume(task.result.downloadUrl!!.toString())
            else cont.resumeWithException(task.exception!!)
        }
        job.invokeOnCompletion(true, true) { uploadTask.removeOnCompleteListener(listener) }
        uploadTask.addOnCompleteListener(listener)
    }
}

fun StorageReference.uploadBytesWithProgress(
    parentContext: CoroutineContext,
    bytes: ByteArray,
    job: Job
): ReceiveChannel<UploadState> = produce(parentContext, parent = job) {
    val channel = Channel<UploadState>()
    val storageTask = this@uploadBytesWithProgress.putBytes(bytes)
    val progressListener = OnProgressListener<UploadTask.TaskSnapshot> { task ->
        launch(coroutineContext) { channel.send(ProgressState((100 * task.bytesTransferred / task.totalByteCount).toInt())) }
    }
    val completeListener = OnCompleteListener<UploadTask.TaskSnapshot> { task ->
        launch(coroutineContext) {
            if (task.isSuccessful) channel.send(UploadedState(task.result.downloadUrl!!.toString()))
            else channel.send(UploadError(task.exception!!))
        }
    }
    storageTask.addOnProgressListener(progressListener).addOnCompleteListener(completeListener)
    job.invokeOnCompletion(true, true) {
        storageTask.removeOnProgressListener(progressListener)
        storageTask.removeOnCompleteListener(completeListener)
        channel.close()
    }
    channel.consumeEach { send(it) }
}