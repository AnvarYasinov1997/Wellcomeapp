package com.wellcome.utils.room

import android.arch.persistence.room.*

@Entity(indices = [(Index(value = ["timestamp"], unique = true))])
data class Story(@PrimaryKey val eventRef: String = "",
                 val timestamp: Long = 0,
                 val isLiked: Boolean = false,
                 val isWillcomed: Boolean = false)

@Dao
interface StoryDao {

    @Query("select * from story")
    fun getAllStories() : List<Story>

    @Query("select * from story where eventRef = :eventRef limit 1")
    fun getStory(eventRef: String): Story


}