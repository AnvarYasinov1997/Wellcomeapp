package wellcome.common.core

import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import wellcome.common.mpp.core.EntityState
import wellcome.common.mpp.entity.StoryData

expect class StoryProvider {
    fun fetchUserStories(userRef: String): Deferred<List<StoryData>>
    fun observeUserStories(job: Job, userRef: String): ReceiveChannel<EntityState<StoryData>>
}
