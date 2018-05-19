package com.wellcome.core.room

import android.arch.persistence.room.*

@Entity(indices = [(Index(value = ["timestamp"], unique = true))])
data class Story(@PrimaryKey var eventRef: String = "",
                 var timestamp: Long = 0,
                 var isLiked: Boolean = false,
                 var isWillcomed: Boolean = false)

@Dao
interface StoryDao {

    @Query("select * from story")
    fun getAllStories() : List<Story>?

    @Query("select * from story where eventRef = :eventRef limit 1")
    fun getStory(eventRef: String): Story?

    @Query("select count(*) from story")
    fun getStoriesCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addStory(story: Story)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addStories(stories: List<Story>)

    @Update
    fun updateStory(story: Story)

    @Update
    fun updateStories(stories: List<Story>)


}