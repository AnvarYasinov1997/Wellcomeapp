package wellcome.common.event

import wellcome.common.mpp.entity.Event
import wellcome.common.mpp.entity.UserData

sealed class EventState
data class StateError(val message: String) : EventState()
data class EventModified(val event: Event) : EventState()
data class UserModified(val eventRef: String, val userData: UserData) : EventState()
data class EventRemoved(val ref: String) : EventState()
data class EventAdded(val event: Event) : EventState()
data class EventsPaginated(val events: List<Event>) : EventState()