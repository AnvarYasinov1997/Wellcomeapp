package com.mistreckless.support.wellcomeapp.data.rxfirebase

import android.annotation.SuppressLint
import com.google.firebase.storage.StorageReference
import wellcome.common.entity.ShareState
import wellcome.common.entity.StateProgress
import wellcome.common.entity.StateUploaded
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import java.io.InputStream

/**
 * Created by @mistreckless on 27.08.2017. !
 */

class RxStorageUploadStream(private val storageRef: StorageReference, private val stream: InputStream) : SingleOnSubscribe<String> {
    @SuppressLint("VisibleForTests")
    override fun subscribe(e: SingleEmitter<String>) {
        storageRef.putStream(stream)
                .addOnSuccessListener {
                    stream.close()
                    if (!e.isDisposed)
                        e.onSuccess(it.downloadUrl.toString())
                }
                .addOnFailureListener {
                    stream.close()
                    if (!e.isDisposed)
                        e.onError(it)
                }
    }
}

class RxStorageUploadBytesWithProgress(private val storageRef: StorageReference,private val bytes : ByteArray) : ObservableOnSubscribe<ShareState>{
    override fun subscribe(e: ObservableEmitter<ShareState>) {
        storageRef.putBytes(bytes)
                .addOnProgressListener {
                    if (!e.isDisposed){
                        e.onNext(StateProgress((100 * it.bytesTransferred/it.totalByteCount).toInt()))
                    }
                }
                .addOnFailureListener {
                    if (!e.isDisposed){
                        e.onError(it)
                    }
                }
                .addOnSuccessListener {
                    if (!e.isDisposed)
                        e.onNext(StateUploaded(it.downloadUrl.toString()))
                }
    }
}
