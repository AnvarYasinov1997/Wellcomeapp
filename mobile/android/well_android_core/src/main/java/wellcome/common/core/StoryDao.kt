package wellcome.common.core

import android.arch.persistence.room.*

@Entity(indices = [(Index(value = ["timestamp"], unique = true))])
actual class Story {
    @PrimaryKey
    actual var eventRef: String = ""
    actual var timestamp: Long = 0
    actual var isLiked: Boolean = false
    actual var isWillcomed: Boolean = false
}

@Dao
actual interface StoryDao {

    @Query("select * from story")
    actual fun getAllStories(): List<Story>?

    @Query("select * from story where eventRef = :eventRef limit 1")
    actual fun getStory(eventRef: String): Story?

    @Query("select count(*) from story")
    actual fun getStoriesCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    actual fun addStory(story: Story)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    actual fun addStories(stories: List<Story>)

    @Update
    actual fun updateStory(story: Story)

    @Update
    actual fun updateStories(stories: List<Story>)

}