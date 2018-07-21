package wellcome.common.core.repository

import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import wellcome.common.mpp.core.EntityState
import wellcome.common.mpp.entity.UserData

interface UserRepository {
    fun getUserReference(): String
    fun observeUserChanges(userRef: String, parentJob: Job): ReceiveChannel<EntityState<UserData>>
}

