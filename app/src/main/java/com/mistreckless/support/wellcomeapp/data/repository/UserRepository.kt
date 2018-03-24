package com.mistreckless.support.wellcomeapp.data.repository

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentListenOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mistreckless.support.wellcomeapp.data.CacheData
import com.mistreckless.support.wellcomeapp.data.rxfirebase.*
import com.mistreckless.support.wellcomeapp.domain.entity.CityData
import com.mistreckless.support.wellcomeapp.domain.entity.UserData
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileInputStream

/**
 * Created by @mistreckless on 05.08.2017. !
 */
interface UserRepository {
    fun getUserReference(): String
    fun findPhoto(data: Uri): Single<String>
    fun uploadPhotoIfNeeded(): Single<String>
    fun observeUserValueEvent(userRef: String): Observable<UserData>
    fun cacheUserData(userData: UserData)
}

class UserRepositoryImpl(private val cacheData: CacheData, private val context: Context) :
    UserRepository {
    override fun getUserReference(): String = cacheData.getString(CacheData.USER_REF)

    override fun findPhoto(data: Uri): Single<String> = Single.create<String>({ emitter ->
        val filePathColumns = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = context.contentResolver.query(data, filePathColumns, null, null, null)
        cursor?.use {
            it.moveToFirst()
            val columnIndex = it.getColumnIndex(filePathColumns[0])
            val path = it.getString(columnIndex)
            emitter.onSuccess(path)
        } ?: emitter.onError(Exception("cannot find photo with path" + data))
    })
        .doOnSuccess { cacheData.cacheString(CacheData.USER_PHOTO, it) }
        .subscribeOn(Schedulers.computation())

    override fun uploadPhotoIfNeeded(): Single<String> {
        val photoPath = cacheData.getString(CacheData.USER_PHOTO)
        if (photoPath.isEmpty()) return Single.just(photoPath)
        val storageRef =
            FirebaseStorage.getInstance().reference.child(cacheData.getString(CacheData.USER_CITY))
                .child(Uri.parse(photoPath).lastPathSegment)
        return storageRef.uploadFileViaStream(FileInputStream(File(photoPath)))
            .subscribeOn(Schedulers.io())
    }

    override fun observeUserValueEvent(userRef: String): Observable<UserData> {
        val fullUserRef =
            FirebaseFirestore.getInstance().collection(FirebaseConstants.CITY).document(userRef)
        return fullUserRef.observeValue(DocumentListenOptions().includeMetadataChanges(), UserData::class.java)
            .subscribeOn(Schedulers.io())
    }

    override fun cacheUserData(userData: UserData) {
        cacheData.cacheString(CacheData.USER_REF, userData.ref)
        cacheData.cacheString(CacheData.USER_ID, userData.id)
        cacheData.cacheString(CacheData.USER_CITY, userData.cityName)
        cacheData.cacheString(CacheData.USER_NAME, userData.displayedName)

        val photoUrl = userData.photoUrl
        if (photoUrl != null && photoUrl.isNotEmpty())
            cacheData.cacheString(CacheData.USER_PHOTO, photoUrl)
    }
}
