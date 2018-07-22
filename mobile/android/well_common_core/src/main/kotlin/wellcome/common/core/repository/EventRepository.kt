package wellcome.common.core.repository

import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import wellcome.common.mpp.core.EntityState
import wellcome.common.mpp.core.ShareState
import wellcome.common.mpp.entity.ContentType
import wellcome.common.mpp.entity.EventData
import kotlin.coroutines.experimental.CoroutineContext

interface EventRepository {
    suspend fun uploadEvents(bytes: ByteArray, job: Job): ReceiveChannel<ShareState>
    fun share(contentType: ContentType,
              url: String,
              userReference: String,
              addressLine: String,
              descLine: String,
              fromTime: Long,
              tillTime: Long,
              tags: List<String>): Job

    fun fetchEvents(lastTimeStamp: Long, limit: Long = 10): Deferred<List<EventData>>
    fun observeEvent(ref: String,
                     parentContext: CoroutineContext,
                     job: Job): ReceiveChannel<EntityState<EventData>>

    fun observeAddedEvents(timestamp: Long,
                           job: Job): ReceiveChannel<EntityState<EventData>>

    fun saveTmpPicture(bytes: ByteArray): Job
    fun getTmpPicture(): Deferred<ByteArray>
    fun addLike(eventRef: String): Job
    fun removeLike(eventRef: String): Job
}