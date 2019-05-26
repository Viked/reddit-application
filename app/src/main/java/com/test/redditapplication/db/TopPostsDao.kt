package com.test.redditapplication.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TopPostsDao {

    @Query("SELECT * FROM Post ORDER BY score DESC")
    fun allPostById(): DataSource.Factory<String, Post>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: List<Post>)

}