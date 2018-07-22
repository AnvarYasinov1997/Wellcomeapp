package wellcome.common.core

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryListenOptions
import com.wellcome.utils.firebase.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce
import wellcome.common.mpp.core.*
import wellcome.common.mpp.entity.StoryData
import kotlin.coroutines.experimental.coroutineContext

actual class StoryProvider {
    actual fun fetchUserStories(userRef: String): Deferred<List<StoryData>> =
        FirebaseFirestore.getInstance().collection(FirebaseConstants.USER).document(userRef).collection(
            FirebaseConstants.STORY).getValues(StoryData::class.java)

    actual fun observeUserStories(job: Job,
                                  userRef: String): ReceiveChannel<EntityState<StoryData>> =
        produce(parent = job) {
            val producer =
                FirebaseFirestore.getInstance().collection(FirebaseConstants.USER).document(userRef)
                        .collection(FirebaseConstants.STORY).observeValues(StoryData::class.java,
                            QueryListenOptions(),
                            coroutineContext,
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
}