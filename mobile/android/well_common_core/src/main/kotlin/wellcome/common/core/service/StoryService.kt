package wellcome.common.core.service

import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.channels.consumeEach
import wellcome.common.core.*
import wellcome.common.mpp.core.*

interface StoryService {
    fun fetchStoriesIfNotExists(): Job
    fun startListen()
    fun stopListen(): Job
    fun getStory(eventRef: String): Deferred<Story>
}

class StoryServiceImpl(private val cache: Cache,
                       private val storyDao: StoryDao,
                       private val storyProvider: StoryProvider) : StoryService {
    private val job by lazy { Job() }

    override fun fetchStoriesIfNotExists(): Job = launch {
        val count = storyDao.getStoriesCount()
        if (count == 0) {
            val data =
                storyProvider.fetchUserStories(cache.getString(CacheConst.USER_REF, "")).await()
            val stories = List(data.size) { i ->
                createStory(data[i].eventRef, data[i].timestamp, data[i].liked, data[i].willcomed)
            }
            storyDao.addStories(stories)
        }
    }

    override fun startListen() {
        launch(parent = job) {
            val channel =
                storyProvider.observeUserStories(job, cache.getString(CacheConst.USER_REF, ""))
            job.invokeOnCompletion {
                channel.cancel()
            }
            channel.consumeEach { storyState ->
                when (storyState) {
                    is EntityAdded    -> {
                        val data = storyState.data
                        log("storry added", data.toString())
                        storyDao.addStory(createStory(data.eventRef,
                            data.timestamp,
                            data.liked,
                            data.willcomed))
                    }
                    is EntityModified -> {
                        val data = storyState.data
                        log("storry modified", data.toString())
                        storyDao.updateStory(createStory(data.eventRef,
                            data.timestamp,
                            data.liked,
                            data.willcomed))
                    }
                    is EntityRemoved  -> log("story removed", storyState.ref)
                    is EntityError    -> log("story error",
                        storyState.exception.message ?: "exception null")
                }
            }
        }
    }

    override fun stopListen(): Job = launch {
        job.cancelChildren()
        job.cancel()
    }

    override fun getStory(eventRef: String): Deferred<Story> = async {
        storyDao.getStory(eventRef) ?: Story()
    }

    private fun createStory(eventRef: String,
                            timestamp: Long,
                            isLiked: Boolean,
                            isWillcomed: Boolean) = Story().apply {
        this.eventRef = eventRef
        this.timestamp = timestamp
        this.isLiked = isLiked
        this.isWillcomed = isWillcomed
    }
}