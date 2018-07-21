package wellcome.common.core.repository

import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.wellcome.utils.firebase.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.launch
import wellcome.common.core.Cache
import wellcome.common.mpp.core.*
import wellcome.common.mpp.entity.*
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.coroutines.experimental.coroutineContext

class EventRepositoryImpl(private val cache: Cache) : EventRepository {
    private lateinit var byteArray: ByteArray //tmp solution

    override suspend fun uploadEvents(bytes: ByteArray, job: Job): ReceiveChannel<ShareState> {
        val path = cache.getString(CacheConst.USER_CITY, "") + System.currentTimeMillis()
        val ref = FirebaseStorage.getInstance().getReference(path)
        val upload = ref.uploadBytesWithProgress(coroutineContext, bytes, job)
        return produce(coroutineContext, parent = job) {
            upload.consumeEach { state ->
                when (state) {
                    is ProgressState -> send(StateProgress(state.progress))
                    is UploadError   -> send(StateError(state.exception))
                    is UploadedState -> send(StateUploaded(state.url))
                }
            }
        }
    }

    override fun share(contentType: ContentType,
                       url: String,
                       userReference: String,
                       addressLine: String,
                       descLine: String,
                       fromTime: Long,
                       tillTime: Long,
                       tags: List<String>): Job = launch {
        val ref = FirebaseFirestore.getInstance().collection(FirebaseConstants.CITY)
                .document(cache.getString(CacheConst.USER_CITY_REF, ""))
                .collection(FirebaseConstants.EVENT).document()
        val post = generatePost(ref.id,
            contentType,
            url,
            userReference,
            addressLine,
            descLine,
            fromTime,
            tillTime)
        ref.setValue(post)
    }

    override fun fetchEvents(lastTimeStamp: Long, limit: Long): Deferred<List<EventData>> = async {
        val ref = FirebaseFirestore.getInstance().collection(FirebaseConstants.CITY)
                .document(cache.getString(CacheConst.USER_CITY_REF, ""))
                .collection(FirebaseConstants.EVENT)
        return@async when (lastTimeStamp) {
            0L   -> ref.orderBy("timestamp", Query.Direction.DESCENDING).limit(limit).getValues(
                EventData::class.java).await()
            else -> ref.orderBy("timestamp",
                Query.Direction.DESCENDING).startAfter(lastTimeStamp).limit(limit).getValues(
                EventData::class.java).await()
        }
    }

    override fun observeEvent(ref: String,
                              parentContext: CoroutineContext,
                              job: Job): ReceiveChannel<EntityState<EventData>> =
        produce(parentContext) {
            val producer = FirebaseFirestore.getInstance().collection(FirebaseConstants.CITY)
                    .document(cache.getString(CacheConst.USER_CITY_REF, ""))
                    .collection(FirebaseConstants.EVENT).document(ref).observeValue(
                        DocumentListenOptions(),
                        EventData::class.java,
                        parentContext,
                        job)

            producer.consumeEach { state ->
                when (state) {
                    is DocumentAdded    -> send(EntityAdded(state.data))
                    is DocumentModified -> send(EntityModified(state.data))
                    is DocumentRemoved  -> send(EntityRemoved(state.ref.id))
                    is DocumentError    -> send(EntityError(state.exception))
                }
            }
        }

    override fun observeAddedEvents(timestamp: Long,
                                    parentContext: CoroutineContext,
                                    job: Job): ReceiveChannel<EntityState<EventData>> =
        produce(parentContext) {
            val producer = FirebaseFirestore.getInstance().collection(FirebaseConstants.CITY)
                    .document(cache.getString(CacheConst.USER_CITY_REF, ""))
                    .collection(FirebaseConstants.EVENT)
                    .orderBy(EventData.TIMESTAMP, Query.Direction.DESCENDING)
                    .whereGreaterThan(EventData.TIMESTAMP, timestamp)
                    .observeAddedValues(EventData::class.java,
                        QueryListenOptions().includeQueryMetadataChanges(),
                        parentContext,
                        job)

            producer.consumeEach { state ->
                when (state) {
                    is DocumentAdded -> send(EntityAdded(state.data))
                }
            }
        }

    override fun saveTmpPicture(bytes: ByteArray): Job = launch {
        byteArray = bytes
    }

    override fun getTmpPicture(): Deferred<ByteArray> = async {
        byteArray
    }

    override fun addLike(eventRef: String): Job {
        val userRef = cache.getString(CacheConst.USER_REF, "")
        val ref = getUserLikeRef(eventRef, userRef)
        val like = Like(userRef)
        return ref.setValue(like)
    }

    override fun removeLike(eventRef: String) =
        getUserLikeRef(eventRef, cache.getString(CacheConst.USER_REF, "")).removeValue()


    private fun generatePost(ref: String,
                             contentType: ContentType,
                             url: String,
                             userReference: String,
                             addressLine: String,
                             descLine: String,
                             fromTime: Long,
                             tillTime: Long): EventData {
        val postContent = ContentData(contentType, userReference, url, descLine, fromTime, tillTime)
        return EventData(ref,
            postContent,
            LatLon(cache.getDouble(CacheConst.TMP_LAT, 0.0),
                cache.getDouble(CacheConst.TMP_LON, 0.0)),
            addressLine,
            cache.getString(CacheConst.USER_CITY, ""),
            EventDataType.SINGLE,
            fromTime)
    }

    private fun getUserLikeRef(eventRef: String, userRef: String): DocumentReference {
        return FirebaseFirestore.getInstance().collection(FirebaseConstants.CITY)
                .document(cache.getString(CacheConst.USER_CITY_REF, ""))
                .collection(FirebaseConstants.EVENT).document(eventRef)
                .collection(FirebaseConstants.LIKE).document(userRef)
    }
}