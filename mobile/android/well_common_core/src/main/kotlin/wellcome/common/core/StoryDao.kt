package wellcome.common.core

expect interface StoryDao {
    fun getAllStories(): List<Story>?
    fun getStory(eventRef: String): Story?
    fun getStoriesCount(): Int
    fun addStory(story: Story)
    fun addStories(stories: List<Story>)
    fun updateStory(story: Story)
    fun updateStories(stories: List<Story>)
}

expect class Story() {
    var eventRef: String
    var timestamp: Long
    var isLiked: Boolean
    var isWillcomed: Boolean
}