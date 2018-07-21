package wellcome.common.event

import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.distinct
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.launch
import wellcome.common.core.Story
import wellcome.common.core.repository.EventRepository
import wellcome.common.core.repository.UserRepository
import wellcome.common.core.service.StoryService
import wellcome.common.mpp.core.EntityAdded
import wellcome.common.mpp.core.EntityModified
import wellcome.common.mpp.core.EntityRemoved
import wellcome.common.mpp.entity.Event
import kotlin.coroutines.experimental.CoroutineContext

interface EventInteractor {
    fun controlPagination(receiveChannel: ReceiveChannel<Int>, job: Job): ReceiveChannel<EventState>

    fun controlEventChanges(event: Event,
                            context: CoroutineContext,
                            job: Job): ReceiveChannel<EventState>

    fun controlAddedEvents(timestamp: Long,
                           context: CoroutineContext,
                           job: Job): ReceiveChannel<EventState>
}


class EventInteractorImpl(private val eventRepository: EventRepository,
                          private val userRepository: UserRepository,
                          private val storyService: StoryService) : EventInteractor {
    override fun controlPagination(receiveChannel: ReceiveChannel<Int>,
                                   job: Job): ReceiveChannel<EventState> = produce(parent = job) {
        var timestamp = 0L
        job.invokeOnCompletion {
            receiveChannel.cancel()
        }
        receiveChannel.distinct().consumeEach {
            val dataList = eventRepository.fetchEvents(timestamp).await()
            timestamp = dataList.lastOrNull()?.timestamp ?: timestamp
            val stories = mutableListOf<Story>()
            for (data in dataList) stories.add(storyService.getStory(data.ref).await())
            val events = List(dataList.size) {
                Event(dataList[it], stories[it].isLiked, stories[it].isWillcomed)
            }
            send(EventsPaginated(events))
        }
    }

    override fun controlEventChanges(event: Event,
                                     context: CoroutineContext,
                                     job: Job): ReceiveChannel<EventState> = produce {
        val eventProducer = eventRepository.observeEvent(event.data.ref, context, job)
        val userProducer = userRepository.observeUserChanges(event.data.content.userRef, job)
        job.invokeOnCompletion {
            eventProducer.cancel()
            userProducer.cancel()
        }
        launch {
            eventProducer.consumeEach { state ->
                when (state) {
                    is EntityModified -> {
                        val story = storyService.getStory(event.data.ref).await()
                        send(EventModified(Event(state.data, story.isLiked, story.isWillcomed)))
                    }
                    is EntityRemoved  -> send(EventRemoved(state.ref))
                    else              -> send(StateError(state.toString()))
                }
            }
        }
        launch {
            userProducer.consumeEach { state ->
                when (state) {
                    is EntityAdded    -> send(UserModified(event.data.ref, state.data))
                    is EntityModified -> send(UserModified(event.data.ref, state.data))
                    else              -> send(StateError(state.toString()))
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
                is EntityAdded -> {
                    val story = storyService.getStory(state.data.ref).await()
                    send(EventAdded(Event(state.data, story.isLiked, story.isWillcomed)))
                }
            }
        }
    }
}