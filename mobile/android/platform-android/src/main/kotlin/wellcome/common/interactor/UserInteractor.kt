package wellcome.common.interactor

import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import wellcome.common.entity.UserData
import wellcome.common.repository.UserRepository

interface UserInteractor{
    fun controlUserChanges(userRef: String, job: Job): ReceiveChannel<UserData>
}

class UserInteractorImpl(private val userRepository: UserRepository) : UserInteractor{
    override fun controlUserChanges(userRef: String, job: Job): ReceiveChannel<UserData> = userRepository.observeUserChanges(userRef, job)

}