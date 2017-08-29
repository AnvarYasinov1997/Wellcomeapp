@file:Suppress("NOTHING_TO_INLINE")

package com.mistreckless.support.wellcomeapp.helper.rxfirebase

import com.google.firebase.storage.StorageReference
import io.reactivex.Single
import java.io.InputStream

/**
 * Created by @mistreckless on 27.08.2017. !
 */

inline fun uploadFileViaStream(storageReference: StorageReference, stream: InputStream): Single<String>
        = Single.create(RxStorageUploadStream(storageReference, stream))