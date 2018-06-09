package wellcome.common.interactor

import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce
import wellcome.common.entity.ContentType
import wellcome.common.entity.ShareState
import wellcome.common.entity.StateUploaded
import wellcome.common.location.LocationService
import wellcome.common.repository.EventRepository
import wellcome.common.repository.UserRepository
import kotlin.coroutines.experimental.CoroutineContext

interface ShareInteractor {
    fun findMyLocation(): Deferred<String>
    fun putPhotoBytes(bytes: ByteArray): Job
    fun getPhotoBytes(): Deferred<ByteArray>
    suspend fun share(
        addressLine: String,
        descLine: String,
        fromTime: Long,
        tillTime: Long,
        context: CoroutineContext,
        parentJob: Job
    ): ReceiveChannel<ShareState>

}

class ShareInteractorImpl(
    private val locationService: LocationService,
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository
) : ShareInteractor {
    override fun findMyLocation(): Deferred<String> = async {
        locationService.getCurrentAddress().run { "$locality $thoroughfare $subThoroughfare" }
    }

    override fun putPhotoBytes(bytes: ByteArray): Job = eventRepository.saveTmpPicture(bytes)

    override fun getPhotoBytes(): Deferred<ByteArray> = eventRepository.getTmpPicture()

    override suspend fun share(
        addressLine: String,
        descLine: String,
        fromTime: Long,
        tillTime: Long,
        context: CoroutineContext,
        parentJob: Job
    ): ReceiveChannel<ShareState> {
        val tags = findTags(descLine)
        val bytes =eventRepository.getTmpPicture().await()
        val producer = eventRepository.uploadEvents(bytes, context, parentJob)
        return produce(context, parent = parentJob) {
            producer.consumeEach {state ->
                when (state) {
                    is StateUploaded -> {
                        val job = eventRepository.share(ContentType.PHOTO, state.url, userRepository.getUserReference(), addressLine, descLine, fromTime, tillTime, tags)
                        job.join()
                        send(state)
                    }
                    else -> send(state)
                }
            }
        }
    }

    private fun findTags(text: String): List<String> {
        return text.split(" ".toRegex()).filter { it.isNotEmpty() && it[0] == '#' }
    }
}