package com.mistreckless.support.wellcomeapp.domain.repository

import com.google.firebase.storage.FirebaseStorage
import com.mistreckless.support.wellcomeapp.domain.CacheData
import com.mistreckless.support.wellcomeapp.domain.entity.ShareState
import com.mistreckless.support.wellcomeapp.util.rxfirebase.uploadBytesWithProgress
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers

/**
 * Created by @mistreckless on 02.09.2017. !
 */
interface PostRepository {
    fun uploadPost(bytes: ByteArray): ObservableSource<ShareState>
}


class PostRepositoryImpl(private val cacheData: CacheData) : PostRepository{
    override fun uploadPost(bytes: ByteArray): ObservableSource<ShareState> {
        val ref = FirebaseStorage.getInstance().getReference(cacheData.getString(CacheData.USER_CITY)+System.currentTimeMillis())
        return ref.uploadBytesWithProgress(bytes)
                .subscribeOn(Schedulers.io())
    }

}