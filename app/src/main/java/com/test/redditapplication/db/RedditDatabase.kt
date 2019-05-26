package com.test.redditapplication.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Post::class], version = 1)
@TypeConverters(Converters::class)
abstract class RedditDatabase : RoomDatabase() {

    companion object {
        fun get(context: Application): RedditDatabase = Room.databaseBuilder(
            context, RedditDatabase::class.java, "RedditDatabase"
        ).build()
    }

    abstract fun topPostsDao(): TopPostsDao
}