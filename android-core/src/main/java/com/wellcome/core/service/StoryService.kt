package com.wellcome.core.service

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryListenOptions
import com.wellcome.core.Cache
import com.wellcome.core.firebase.*
import com.wellcome.core.room.Story
import com.wellcome.core.room.StoryDao
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.channels.consumeEach
import wellcome.common.core.CacheConst
import wellcome.common.core.FirebaseConstants
import wellcome.common.entity.StoryData

interface StoryService {
    fun startListen()
    fun stopListen()
    fun getStory(eventRef: String): Deferred<Story>
}

class WellcomeStoryService(private val storyDao: StoryDao,
                           private val cache: Cache) : StoryService {
    private val job by lazy { Job() }


    override fun getStory(eventRef: String): Deferred<Story> = async{
        storyDao.getStory(eventRef) ?: Story()
    }

    override fun startListen() {
        launch(parent = job) {
            val channel = FirebaseFirestore.getInstance().collection(FirebaseConstants.USER)
                .document(cache.getString(CacheConst.USER_REF, ""))
                .collection(FirebaseConstants.STORY)
                .observeValues(StoryData::class.java, QueryListenOptions(), coroutineContext, job)
            job.invokeOnCompletion {
                channel.cancel()
            }
            channel.consumeEach { storyState ->
                when (storyState) {
                    is DocumentAdded -> {
                        val data = storyState.data
                        Log.e("storyAdded",data.toString())
                        storyDao.addStory(Story(data.eventRef,data.timestamp,data.liked,data.willcomed))
                    }
                    is DocumentModified -> {
                        val data = storyState.data
                        Log.e("storyModified",data.toString())
                        storyDao.updateStory(Story(data.eventRef,data.timestamp,data.liked,data.willcomed))
                    }
                    is DocumentRemoved -> {
                        Log.e("storyRemoved",storyState.ref.id)
                    }
                    is DocumentError -> Log.e("storyError",storyState.ref?.id ?: "null", storyState.exception)
                }
            }
        }
    }

    override fun stopListen() {
        launch {
            job.cancelChildren()
            job.cancel()
        }
    }
}