package com.mistreckless.support.wellcomeapp.domain.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mistreckless.support.wellcomeapp.domain.CacheData
import com.mistreckless.support.wellcomeapp.domain.entity.ContentData
import com.mistreckless.support.wellcomeapp.domain.entity.PostData
import com.mistreckless.support.wellcomeapp.domain.entity.PostType
import com.mistreckless.support.wellcomeapp.domain.entity.ShareState
import com.mistreckless.support.wellcomeapp.util.rxfirebase.uploadBytesWithProgress
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
        val post = PostData()
    }


    private fun generatePost(ref : String,postType: PostType,url: String, userReference: String, addressLine: String, descLine: String, dressControl: Boolean, ageLine: String, fromTime: Long, tillTime: Long) : PostData{
        val postContent = ContentData(postType,userReference,url,descLine,fromTime,tillTime)
        val postData = PostData(ref, mutableListOf(postContent),)
    }
}