package com.mistreckless.support.wellcomeapp.domain.repository

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.mistreckless.support.wellcomeapp.domain.CacheData
import com.mistreckless.support.wellcomeapp.domain.entity.RatingData
import com.mistreckless.support.wellcomeapp.domain.entity.UserData
import com.mistreckless.support.wellcomeapp.helper.rxfirebase.setValue
import com.mistreckless.support.wellcomeapp.helper.rxfirebase.signInWithCredential
import com.mistreckless.support.wellcomeapp.helper.rxfirebase.singleQuery
import com.mistreckless.support.wellcomeapp.helper.rxfirebase.uploadFileViaStream
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileInputStream

/**
 * Created by @mistreckless on 05.08.2017. !
 */
interface UserRepository {
    fun getUserReference(): String
    fun signUpWithGoogle(account: GoogleSignInAccount): Single<FirebaseUser>
    fun isUserAlreadyRegistered(uid: String): Single<Boolean>
    fun registryNewUser(uid: String, fullName: String?, displayedName: String, photoUrl : String): Completable
    fun findPhoto(data: Uri): Single<String>
    fun uploadPhotoIfNeeded(): Single<String>
}

class UserRepositoryImpl(private val cacheData: CacheData, private val context: Context) : UserRepository {
    override fun getUserReference(): String = cacheData.getString(CacheData.USER_REF)

    override fun signUpWithGoogle(account: GoogleSignInAccount): Single<FirebaseUser> {
        val instance = FirebaseAuth.getInstance()
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        return signInWithCredential(instance, credential)
    }

    override fun isUserAlreadyRegistered(uid: String): Single<Boolean> {
        return singleQuery(FirebaseDatabase.getInstance().getReference("users").orderByChild("id"), UserData::class.java)
                .doOnSuccess { cacheUserData(it) }
                .map { true }
                .onErrorReturn { false }

    }

    override fun registryNewUser(uid: String, fullName: String?, displayedName: String, photoUrl: String): Completable {
        val userRef = FirebaseDatabase.getInstance().getReference("users").push()
        val ratingRef = FirebaseDatabase.getInstance().getReference("ratings").push()
        val cityName = cacheData.getString(CacheData.USER_CITY)
        if (cityName.isEmpty()) return Completable.error(Exception("city is empty"))
        val userData = UserData(uid, userRef.key, ratingRef.key, cityName, fullName, displayedName, photoUrl)
        val ratingData = RatingData()
        return setValue(userRef, userData)
                .mergeWith(setValue(ratingRef, ratingData))
                .doOnComplete { cacheUserData(userData) }
                .subscribeOn(Schedulers.io())
    }

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
        val storageRef = FirebaseStorage.getInstance().reference.child(cacheData.getString(CacheData.USER_CITY)).child(Uri.parse(photoPath).lastPathSegment)
        return uploadFileViaStream(storageRef, FileInputStream(File(photoPath)))
                .subscribeOn(Schedulers.io())
    }

    private fun cacheUserData(userData: UserData) {
        cacheData.cacheString(CacheData.USER_REF, userData.ref)
        cacheData.cacheString(CacheData.USER_ID, userData.id)
        cacheData.cacheString(CacheData.USER_CITY, userData.cityName)
        cacheData.cacheString(CacheData.USER_DISPLAYED_NAME, userData.displayedName)
        cacheData.cacheString(CacheData.RATING_REF, userData.ratingRef)

        val photoUrl = userData.photoUrl
        if (photoUrl != null && photoUrl.isNotEmpty())
            cacheData.cacheString(CacheData.USER_PHOTO, photoUrl)
    }
}
