package wellcome.common.interactor

import com.wellcome.core.firebase.DocumentAdded
import com.wellcome.core.firebase.DocumentError
import com.wellcome.core.firebase.DocumentModified
import com.wellcome.core.firebase.DocumentRemoved
import com.wellcome.core.room.Story
import com.wellcome.core.service.StoryService
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.distinct
import kotlinx.coroutines.experimental.channels.produce
import wellcome.common.entity.*
import wellcome.common.repository.EventRepository
import kotlin.coroutines.experimental.CoroutineContext

interface EventInteractor {
    fun controlPagination(receiveChannel: ReceiveChannel<Int>,
                          job: Job): ReceiveChannel<List<Event>>

    fun controlEventChanges(ref: String,
                            context: CoroutineContext,
                            job: Job): ReceiveChannel<EventState>

    fun controlAddedEvents(timestamp: Long,
                           context: CoroutineContext,
                           job: Job): ReceiveChannel<EventState>

    fun like(ref: String, isLiked: Boolean): Job
}

class EventInteractorImpl(private val eventRepository: EventRepository,
                          private val storyService: StoryService) : EventInteractor {
    override fun controlPagination(receiveChannel: ReceiveChannel<Int>,
                                   job: Job): ReceiveChannel<List<Event>> =
        produce(parent = job) {
            var timestamp = 0L
            receiveChannel.distinct().consumeEach {
                val dataList = eventRepository.fetchEvents(timestamp).await()
                timestamp = dataList.lastOrNull()?.timestamp ?: timestamp
                val stories = mutableListOf<Story>()
                for (data in dataList) stories.add(storyService.getStory(data.ref).await())
                val events = List(dataList.size) {
                    Event(dataList[it], stories[it].isLiked, stories[it].isWillcomed)
                }
                send(events)
            }
            job.invokeOnCompletion {
                receiveChannel.cancel()
            }
        }

    override fun controlEventChanges(ref: String,
                                     context: CoroutineContext,
                                     job: Job): ReceiveChannel<EventState> = produce {
        val producer = eventRepository.observeEvent(ref, context, job)
        job.invokeOnCompletion {
            producer.cancel()
        }
        producer.consumeEach { state ->
            when (state) {
                is DocumentAdded -> {
                } // do nothing
                is DocumentModified -> {
                    val story = storyService.getStory(state.ref.id).await()
                    send(EventModified(Event(state.data, story.isLiked, story.isWillcomed)))
                }
                is DocumentRemoved -> {
                    send(EventRemoved(state.ref.id))
                }
                is DocumentError -> {
                    send(EventError(state.exception))
                }
            }
        }
    }

    override fun controlAddedEvents(timestamp: Long,
                                    context: CoroutineContext,
                                    job: Job): ReceiveChannel<EventState> = produce {
        val producer = eventRepository.observeAddedEvents(timestamp, context, job)
        job.invokeOnCompletion {
            producer.cancel()
        }
        producer.consumeEach { state ->
            when (state) {
                is DocumentAdded -> {
                    val story = storyService.getStory(state.ref.id).await()
                    send(EventAdded(Event(state.data, story.isLiked, story.isWillcomed)))
                }
            }
        }
    }

    override fun like(ref: String, isLiked: Boolean) =
        if (isLiked) eventRepository.removeLike(ref) else eventRepository.addLike(ref)

}