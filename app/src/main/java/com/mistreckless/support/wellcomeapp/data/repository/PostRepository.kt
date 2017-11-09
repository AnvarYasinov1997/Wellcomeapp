package com.mistreckless.support.wellcomeapp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mistreckless.support.wellcomeapp.data.CacheData
import com.mistreckless.support.wellcomeapp.domain.entity.*
import com.mistreckless.support.wellcomeapp.data.rxfirebase.setValue
import com.mistreckless.support.wellcomeapp.data.rxfirebase.uploadBytesWithProgress
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers

/**
 * Created by @mistreckless on 02.09.2017. !
 */
interface PostRepository {
    fun uploadPost(bytes: ByteArray): ObservableSource<ShareState>
    fun share(postType: PostType,url: String, userReference: String, addressLine: String, descLine: String, dressControl: Boolean, ageLine: String, fromTime: Long, tillTime: Long, tags: List<String>): Observable<ShareState>
}


class PostRepositoryImpl(private val cacheData: CacheData) : PostRepository{
    override fun uploadPost(bytes: ByteArray): ObservableSource<ShareState> {
        val ref = FirebaseStorage.getInstance().getReference(cacheData.getString(CacheData.USER_CITY)+System.currentTimeMillis())
        return ref.uploadBytesWithProgress(bytes)
                .subscribeOn(Schedulers.io())
    }

    override fun share(postType: PostType,url: String, userReference: String, addressLine: String, descLine: String, dressControl: Boolean, ageLine: String, fromTime: Long, tillTime: Long, tags: List<String>): Observable<ShareState> {
        val ref = FirebaseFirestore.getInstance().collection("city").document(cacheData.getString(CacheData.USER_CITY_REF)).collection("post").document()
        val post = generatePost(ref.id, postType,url, userReference, addressLine, descLine, dressControl, ageLine, fromTime, tillTime)
        return ref.setValue(post).toSingle<ShareState> { StateDone() }.toObservable()
    }


    private fun generatePost(ref : String,postType: PostType,url: String, userReference: String, addressLine: String, descLine: String, dressControl: Boolean, ageLine: String, fromTime: Long, tillTime: Long) : PostData{
        val postContent = ContentData(postType,userReference,url,descLine,fromTime,tillTime)
        val postData = PostData(ref, mutableListOf(postContent), Pair(cacheData.getDouble(CacheData.TMP_LAT),cacheData.getDouble(CacheData.TMP_LON)),addressLine,cacheData.getString(CacheData.USER_CITY), dressControl,ageLine)
        return postData
    }
}