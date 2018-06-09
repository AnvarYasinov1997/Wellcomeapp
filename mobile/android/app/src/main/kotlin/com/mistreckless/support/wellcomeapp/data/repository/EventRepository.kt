package com.mistreckless.support.wellcomeapp.data.repository

import com.google.firebase.firestore.DocumentListenOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryListenOptions
import com.google.firebase.storage.FirebaseStorage
import com.mistreckless.support.wellcomeapp.data.CacheData
import com.mistreckless.support.wellcomeapp.data.rxfirebase.*
import wellcome.common.entity.*
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

interface EventRepository {
    fun uploadEvent(bytes: ByteArray): ObservableSource<ShareState>
    fun share(
        contentType: ContentType,
        url: String,
        userReference: String,
        addressLine: String,
        descLine: String,
        fromTime: Long,
        tillTime: Long,
        tags: List<String>
    ): Observable<ShareState>

    fun fetchEvents(lastTimeStamp: Long, limit: Long = 10): Single<List<EventData>>
    fun observeEvent(ref: String): Observable<DocumentState<EventData>>
    fun observeEvents(timestamp: Long): Observable<DocumentState<EventData>>
}


class EventRepositoryImpl(private val cacheData: CacheData) : EventRepository {
    override fun observeEvent(ref: String): Observable<DocumentState<EventData>> =
        FirebaseFirestore.getInstance().collection(FirebaseConstants.CITY)
            .document(cacheData.getString(CacheData.USER_CITY_REF))
            .collection(FirebaseConstants.EVENT).document(ref)
            .observeValue(DocumentListenOptions().includeMetadataChanges(), EventData::class.java)
            .subscribeOn(Schedulers.io())

    override fun observeEvents(timestamp: Long): Observable<DocumentState<EventData>> =
        FirebaseFirestore.getInstance().collection(FirebaseConstants.CITY)
            .document(cacheData.getString(CacheData.USER_CITY_REF))
            .collection(FirebaseConstants.EVENT)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .whereGreaterThan("timestamp", timestamp)
            .observeValues(
                QueryListenOptions().includeQueryMetadataChanges(),
                EventData::class.java
            )
            .subscribeOn(Schedulers.io())

    override fun uploadEvent(bytes: ByteArray): ObservableSource<ShareState> {
        val ref = FirebaseStorage.getInstance()
            .getReference(cacheData.getString(CacheData.USER_CITY) + System.currentTimeMillis())
        return ref.uploadBytesWithProgress(bytes)
            .subscribeOn(Schedulers.io())
    }

    override fun share(
        contentType: ContentType,
        url: String,
        userReference: String,
        addressLine: String,
        descLine: String,
        fromTime: Long,
        tillTime: Long,
        tags: List<String>
    ): Observable<ShareState> {
        val ref = FirebaseFirestore.getInstance().collection(FirebaseConstants.CITY)
            .document(cacheData.getString(CacheData.USER_CITY_REF))
            .collection(FirebaseConstants.EVENT).document()
        val post = generatePost(
            ref.id,
            contentType,
            url,
            userReference,
            addressLine,
            descLine,
            fromTime,
            tillTime
        )
        return ref.setValue(post).toSingle<ShareState> { StateError(Exception()) }.toObservable()
    }

    override fun fetchEvents(lastTimeStamp: Long, limit: Long): Single<List<EventData>> {
        val ref = FirebaseFirestore.getInstance()
            .collection(FirebaseConstants.CITY)
            .document(cacheData.getString(CacheData.USER_CITY_REF))
            .collection(FirebaseConstants.EVENT)

        return when (lastTimeStamp) {
            0L -> ref.orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(limit)
                .getValues(EventData::class.java)
                .subscribeOn(Schedulers.io())
            else -> ref.orderBy("timestamp", Query.Direction.DESCENDING)
                .startAfter(lastTimeStamp)
                .limit(limit)
                .getValues(EventData::class.java)
                .subscribeOn(Schedulers.io())
        }
    }

    private fun generatePost(
        ref: String,
        contentType: ContentType,
        url: String,
        userReference: String,
        addressLine: String,
        descLine: String,
        fromTime: Long,
        tillTime: Long
    ): EventData {
        val postContent = ContentData(contentType, userReference, url, descLine, fromTime, tillTime)
        return EventData(
            ref,
            mutableListOf(postContent),
            LatLon(cacheData.getDouble(CacheData.TMP_LAT), cacheData.getDouble(CacheData.TMP_LON)),
            addressLine,
            cacheData.getString(CacheData.USER_CITY),
            EventDataType.SINGLE,
            fromTime
        )
    }

}