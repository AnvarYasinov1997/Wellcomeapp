package com.wellcome.utils.firebase

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.channels.*
import java.io.InputStream
import kotlin.coroutines.experimental.CoroutineContext

//fun StorageReference.uploadFileViaStream(stream: InputStream): Deferred<String> = async {
//    suspendCancellableCoroutine<String> { cont ->
//        this@uploadFileViaStream.putStream(stream).addOnCompleteListener { task ->
//            stream.close()
//            if (task.isSuccessful) cont.resume(task.result.downloadUrl!!.toString())
//            else cont.resumeWithException(task.exception!!)
//        }
//    }
//}

fun StorageReference.uploadBytesWithProgress(
    parentContext: CoroutineContext,
    bytes: ByteArray,
    job: Job
): ReceiveChannel<UploadState> = produce(parentContext,parent = job) {
    val channel = Channel<UploadState>()
    val storageTask = this@uploadBytesWithProgress.putBytes(bytes)
    val progressListener = OnProgressListener<UploadTask.TaskSnapshot> {task ->
        launch(coroutineContext) { channel.send(ProgressState((100 * task.bytesTransferred / task.totalByteCount).toInt())) }
    }
    val completeListener = OnCompleteListener<UploadTask.TaskSnapshot>{ task ->
        launch(coroutineContext) {
            if (task.isSuccessful) channel.send(UploadedState(task.result.downloadUrl!!.toString()))
            else channel.send(UploadError(task.exception!!))
        }
    }
    storageTask.addOnProgressListener(progressListener).addOnCompleteListener(completeListener)
    job.invokeOnCompletion(true,true){
        storageTask.removeOnProgressListener(progressListener)
        storageTask.removeOnCompleteListener(completeListener)
        channel.close()
    }
    channel.consumeEach { send(it) }
}