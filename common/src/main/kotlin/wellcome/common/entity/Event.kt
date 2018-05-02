package wellcome.common.entity

sealed class ShareState

class StateProgress(val progress: Int = 0) : ShareState()
class StateUploaded(val url: String) : ShareState()
class StateError(val exception: Exception) : ShareState()


data class EventData(val ref: String = "",
                     val contents: MutableList<ContentData> = mutableListOf(),
                     val latLon: LatLon = LatLon(),
                     val address: String = "",
                     val city: String = "",
                     val eventDataType: EventDataType = EventDataType.SINGLE,
                     val timestamp: Long = 0,
                     val likeCount: Int = 0,
                     val commentCount: Int = 0,
                     val willcomeCount: Int = 0,
                     val reportCount: Int = 0){
    companion object {
        const val REF ="ref"
        const val TIMESTAMP = "timestamp"
    }
}

enum class EventDataType {
    MULTI, SINGLE
}

enum class ContentType {
    PHOTO, VIDEO
}

data class ContentData(val contentType: ContentType = ContentType.PHOTO,
                       val userRef: String = "",
                       val content: String = "",
                       val desc: String = "",
                       val createTime: Long = 0,
                       val deleteTime: Long = 0)

data class LatLon(val lat: Double = 0.0,
                  val lon: Double = 0.0)

sealed class EventState
data class EventAdded(val event: EventData): EventState()
data class EventModified(val event: EventData): EventState()
data class EventRemoved(val ref: String): EventState()
data class EventError(val exception: Exception): EventState()
