package wellcome.common.interactor

import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.distinct
import kotlinx.coroutines.experimental.channels.produce
import wellcome.common.entity.EventData
import wellcome.common.entity.EventState
import wellcome.common.repository.EventRepository
import kotlin.coroutines.experimental.CoroutineContext

interface EventInteractor {
    fun controlPagination(receiveChannel: ReceiveChannel<Int>,
                          job: Job): ReceiveChannel<List<EventData>>

    fun controlEventChanges(ref: String,
                            context: CoroutineContext,
                            job: Job): ReceiveChannel<EventState>

    fun controlAddedEvents(timestamp: Long,
                           context: CoroutineContext,
                           job: Job): ReceiveChannel<EventState>

    fun like(ref: String) : Job
}

class EventInteractorImpl(private val eventRepository: EventRepository) : EventInteractor {
    override fun controlPagination(receiveChannel: ReceiveChannel<Int>,
                                   job: Job): ReceiveChannel<List<EventData>> =
        produce(parent = job) {
            var timestamp = 0L
            receiveChannel.distinct().consumeEach {
                val events = eventRepository.fetchEvents(timestamp).await()
                timestamp = events.lastOrNull()?.timestamp ?: timestamp
                send(events)
            }
            job.invokeOnCompletion {
                receiveChannel.cancel()
            }
        }

    override fun controlEventChanges(ref: String,
                                     context: CoroutineContext,
                                     job: Job): ReceiveChannel<EventState> =
        eventRepository.observeEvent(ref, context, job)

    override fun controlAddedEvents(timestamp: Long,
                                    context: CoroutineContext,
                                    job: Job): ReceiveChannel<EventState> =
        eventRepository.observeAddedEvents(timestamp, context, job)

    override fun like(ref: String) =
        eventRepository.addLike(ref)

}