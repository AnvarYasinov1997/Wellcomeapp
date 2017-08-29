package com.mistreckless.support.wellcomeapp.helper.rxfirebase

import android.annotation.SuppressLint
import com.google.firebase.storage.StorageReference
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