package com.wellcome.core.service

import com.google.firebase.firestore.FirebaseFirestore
import com.wellcome.core.Cache
import com.wellcome.core.room.StoryDao
import wellcome.common.core.CacheConst
import wellcome.common.core.FirebaseConstants

interface StoryService {
    fun startListen()
    fun stopListen()
}

class WellcomeStoryService(private val storyDao: StoryDao, private val cache: Cache) :
    StoryService {
    override fun startListen() {
        FirebaseFirestore.getInstance().collection(FirebaseConstants.USER)
            .document(cache.getString(CacheConst.USER_REF, ""))
            .collection(FirebaseConstants.STORY)

    }

    override fun stopListen() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}