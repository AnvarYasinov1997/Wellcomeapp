package wellcome.common.event

import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.NonCancellable.isActive
import kotlinx.coroutines.experimental.cancelChildren
import kotlinx.coroutines.experimental.channels.*
import kotlinx.coroutines.experimental.launch
import wellcome.common.core.repository.EventRepository
import wellcome.common.core.repository.UserRepository
import wellcome.common.core.service.StoryService
import wellcome.common.mpp.core.EntityAdded
import wellcome.common.mpp.core.EntityModified
import wellcome.common.mpp.core.EntityRemoved
import wellcome.common.mpp.core.EntityState
import wellcome.common.mpp.entity.Event
import wellcome.common.mpp.entity.EventData
import kotlin.coroutines.experimental.CoroutineContext

interface EventInteractor {
    fun controlEventChanges(event: Event,
                            context: CoroutineContext,
                            job: Job): ReceiveChannel<EventState>

    fun controlEvents(job: Job, receiveChannel: ReceiveChannel<Int>): ReceiveChannel<EventState>
}


class EventInteractorImpl(private val eventRepository: EventRepository,
                          private val userRepository: UserRepository,
                          private val storyService: StoryService) : EventInteractor {
    override fun controlEvents(job: Job,
                               receiveChannel: ReceiveChannel<Int>): ReceiveChannel<EventState> =
        produce(parent = job) {
            var lastTimestamp = 0L
            job.invokeOnCompletion {
                receiveChannel.cancel()
                job.cancelChildren()
            }
            receiveChannel.distinct().consumeEachIndexed { index ->
                val dataList = eventRepository.fetchEvents(lastTimestamp).await()
                lastTimestamp = dataList.lastOrNull()?.timestamp ?: lastTimestamp
                val events = convertDataToEvents(dataList)
                send(EventsPaginated(events))

                if (index.index == 1) {
                    observeAddedEvents(job, dataList.firstOrNull()?.timestamp ?: 0L, channel)
                }
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

    private suspend fun convertDataToEvents(dataList: List<EventData>): List<Event> {
        val stories = List(dataList.size) { i ->
            storyService.getStory(dataList[i].ref).await()
        }
        return List(dataList.size) { i ->
            Event(dataList[i], stories[i].isLiked, stories[i].isWillcomed)
        }
    }

    private suspend fun observeAddedEvents(job: Job,
                                           timestamp: Long,
                                           channel: SendChannel<EventState>) {
        var firstTimestamp = timestamp
        var tmpTimestamp = -1L
        var addedJob = Job(parent = job)
        var producer: ReceiveChannel<EntityState<EventData>>? = null
        addedJob.invokeOnCompletion {
            producer?.cancel()
        }

        while (isActive) {
            if (tmpTimestamp < firstTimestamp) {
                addedJob.cancelChildren()
                addedJob.cancel()
                addedJob = Job(parent = job)

                producer = eventRepository.observeAddedEvents(firstTimestamp, job)
                launch(parent = addedJob) {
                    producer.consumeEach { state ->
                        when (state) {
                            is EntityAdded -> {
                                tmpTimestamp = firstTimestamp
                                firstTimestamp = state.data.timestamp
                                val story = storyService.getStory(state.data.ref).await()
                                channel.send(EventAdded(Event(state.data,
                                    story.isLiked,
                                    story.isWillcomed)))
                            }
                            else           -> channel.send(StateError("unexpected state"))
                        }
                    }
                }
            }
        }
    }
}