package wellcome.common.core.repository

import com.google.firebase.firestore.DocumentListenOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.wellcome.utils.firebase.*
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce
import wellcome.common.core.Cache
import wellcome.common.mpp.core.*
import wellcome.common.mpp.entity.UserData
import kotlin.coroutines.experimental.coroutineContext

class UserRepositoryImpl(private val cache: Cache) : UserRepository {
    override fun getUserReference(): String = cache.getString(CacheConst.USER_REF, "")

    override fun observeUserChanges(userRef: String,
                                    parentJob: Job): ReceiveChannel<EntityState<UserData>> =
        produce(parent = parentJob) {
            val producer =
                FirebaseFirestore.getInstance().collection(FirebaseConstants.USER).document(userRef)
                        .observeValue(DocumentListenOptions(),
                            UserData::class.java,
                            coroutineContext,
                            parentJob,
                            false)
            parentJob.invokeOnCompletion { producer.cancel() }
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