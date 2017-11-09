@file:Suppress("NOTHING_TO_INLINE")

package com.mistreckless.support.wellcomeapp.data.rxfirebase

import com.google.firebase.storage.StorageReference
import com.mistreckless.support.wellcomeapp.domain.entity.ShareState
import io.reactivex.Observable
import io.reactivex.Single
import java.io.InputStream

/**
 * Created by @mistreckless on 27.08.2017. !
 */

inline fun StorageReference.uploadFileViaStream(stream: InputStream): Single<String>
        = Single.create(RxStorageUploadStream(this, stream))

inline fun StorageReference.uploadBytesWithProgress(bytes: ByteArray): Observable<ShareState>
        = Observable.create(RxStorageUploadBytesWithProgress(this,bytes))