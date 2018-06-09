package wellcome.common.entity

data class StoryData(val eventRef: String = "",
                     val timestamp: Long = 0,
                     val liked: Boolean = false,
                     val willcomed: Boolean = false)