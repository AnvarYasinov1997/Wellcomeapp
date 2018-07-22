package com.wellcome.wellcomeapp.db

import android.app.Application
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import wellcome.common.core.Story
import wellcome.common.core.StoryDao

@Database(entities = [Story::class],version = 1)
abstract class WellcomeDatabase: RoomDatabase() {
    abstract fun storyDao(): StoryDao
}

inline fun buildWellcomeDb(context: Application, name: String) =
    Room.databaseBuilder(context, WellcomeDatabase::class.java, name).build()