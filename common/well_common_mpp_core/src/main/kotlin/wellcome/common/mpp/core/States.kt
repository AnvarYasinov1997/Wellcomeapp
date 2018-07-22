package wellcome.common.mpp.core

sealed class EntityState<T>
data class EntityAdded<T>(val data: T) : EntityState<T>()
data class EntityModified<T>(val data: T) : EntityState<T>()
data class EntityRemoved<T>(val ref: String) : EntityState<T>()
data class EntityError<T>(val exception: Exception) : EntityState<T>()

sealed class ShareState
class StateProgress(val progress: Int = 0) : ShareState()
class StateUploaded(val url: String) : ShareState()
class StateError(val exception: Exception) : ShareState()