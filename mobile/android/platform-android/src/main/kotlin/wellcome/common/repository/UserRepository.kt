package wellcome.common.repository

import com.google.firebase.firestore.DocumentListenOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.wellcome.core.Cache
import com.wellcome.core.firebase.DocumentAdded
import com.wellcome.core.firebase.DocumentModified
import com.wellcome.core.firebase.observeValue
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce
import wellcome.common.core.CacheConst
import wellcome.common.core.FirebaseConstants
import wellcome.common.entity.UserData

class UserRepository(private val cache: Cache) {
    fun getUserReference(): String = cache.getString(CacheConst.USER_REF, "")

    fun observeUserChanges(ref: String, parentJob: Job) = produce(parent = parentJob) {
        val producer = FirebaseFirestore.getInstance().collection(FirebaseConstants.USER)
            .document(ref)
            .observeValue(DocumentListenOptions(),
                UserData::class.java,
                coroutineContext,
                parentJob,
                false)
        parentJob.invokeOnCompletion { producer.cancel() }
        producer.consumeEach { state ->
            when (state) {
                is DocumentAdded -> send(state.data)
                is DocumentModified -> send(state.data)
            }
        }
    }
}