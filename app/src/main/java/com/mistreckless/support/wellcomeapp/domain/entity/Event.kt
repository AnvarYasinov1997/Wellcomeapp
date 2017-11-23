package com.mistreckless.support.wellcomeapp.domain.entity

/**
 * Created by @mistreckless on 14.10.2017. !
 */

sealed class ShareState

class StateInit : ShareState()
class StateUpload(val progress: Int = 0) : ShareState()
class StateUploaded(val url: String) : ShareState()
class StateDone() : ShareState()
class StateError(val message: String) : ShareState()


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
                     val reportCount: Int = 0)

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
