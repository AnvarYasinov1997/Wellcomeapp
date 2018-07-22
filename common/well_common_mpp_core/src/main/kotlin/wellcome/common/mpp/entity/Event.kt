package wellcome.common.mpp.entity



data class Event(val data: EventData,
                 val isLiked: Boolean = false,
                 val isWillcomed: Boolean = false)

data class EventData(val ref: String = "", val content: ContentData = ContentData(),
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

        const val LIKE_COUNT = "likeCount"
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

