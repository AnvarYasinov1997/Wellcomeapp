@file:Suppress("NOTHING_TO_INLINE")

package com.wellcomeapp.main.data.rxfirebase

import com.google.firebase.storage.StorageReference
import io.reactivex.Observable
import io.reactivex.Single
import wellcome.common.entity.ShareState
import java.io.InputStream

/**
 * Created by @mistreckless on 27.08.2017. !
 */

inline fun StorageReference.uploadFileViaStream(stream: InputStream): Single<String> = Single.create(RxStorageUploadStream(this, stream))

inline fun StorageReference.uploadBytesWithProgress(bytes: ByteArray): Observable<ShareState> = Observable.create(RxStorageUploadBytesWithProgress(this, bytes))