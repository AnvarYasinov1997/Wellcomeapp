package com.mistreckless.support.wellcomeapp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mistreckless.support.wellcomeapp.data.CacheData
import com.mistreckless.support.wellcomeapp.data.rxfirebase.FirebaseConstants
import com.mistreckless.support.wellcomeapp.data.rxfirebase.getValues
import com.mistreckless.support.wellcomeapp.data.rxfirebase.setValue
import com.mistreckless.support.wellcomeapp.data.rxfirebase.uploadBytesWithProgress
import com.mistreckless.support.wellcomeapp.domain.entity.*
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

/**
 * Created by @mistreckless on 02.09.2017. !
 */
interface EventRepository {
    fun uploadEvent(bytes: ByteArray): ObservableSource<ShareState>
    fun share(contentType: ContentType, url: String, userReference: String, addressLine: String, descLine: String, fromTime: Long, tillTime: Long, tags: List<String>): Observable<ShareState>
    fun fetchEvents(lastTimeStamp: Long,limit : Long=10): Single<List<EventData>>
}


class EventRepositoryImpl(private val cacheData: CacheData) : EventRepository {
    override fun uploadEvent(bytes: ByteArray): ObservableSource<ShareState> {
        val ref = FirebaseStorage.getInstance().getReference(cacheData.getString(CacheData.USER_CITY) + System.currentTimeMillis())
        return ref.uploadBytesWithProgress(bytes)
                .subscribeOn(Schedulers.io())
    }

    override fun share(contentType: ContentType, url: String, userReference: String, addressLine: String, descLine: String, fromTime: Long, tillTime: Long, tags: List<String>): Observable<ShareState> {
        val ref = FirebaseFirestore.getInstance().collection(FirebaseConstants.CITY).document(cacheData.getString(CacheData.USER_CITY_REF)).collection(FirebaseConstants.EVENT).document()
        val post = generatePost(ref.id, contentType, url, userReference, addressLine, descLine, fromTime, tillTime)
        return ref.setValue(post).toSingle<ShareState> { StateDone() }.toObservable()
    }

    override fun fetchEvents(lastTimeStamp: Long,limit: Long): Single<List<EventData>> {
        val ref = FirebaseFirestore.getInstance()
                .collection(FirebaseConstants.CITY)
                .document(cacheData.getString(CacheData.USER_CITY_REF))
                .collection(FirebaseConstants.EVENT)

        return when(lastTimeStamp){
            0L->ref.orderBy("timestamp")
                    .limit(limit)
                    .getValues(EventData::class.java)
                    .subscribeOn(Schedulers.io())
            else->ref.orderBy("timestamp").startAfter(lastTimeStamp)
                    .getValues(EventData::class.java)
                    .subscribeOn(Schedulers.io())
        }
    }

    private fun generatePost(ref: String, contentType: ContentType, url: String, userReference: String, addressLine: String, descLine: String, fromTime: Long, tillTime: Long): EventData {
        val postContent = ContentData(contentType, userReference, url, descLine, fromTime, tillTime)
        return EventData(ref, mutableListOf(postContent), LatLon(cacheData.getDouble(CacheData.TMP_LAT), cacheData.getDouble(CacheData.TMP_LON)), addressLine, cacheData.getString(CacheData.USER_CITY),EventDataType.SINGLE)
    }
}