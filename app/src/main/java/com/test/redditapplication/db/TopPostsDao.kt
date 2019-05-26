package com.test.redditapplication.db

import androidx.paging.DataSource
import androidx.room.*

@Dao
abstract class TopPostsDao {

    @Query("SELECT * FROM Post ORDER BY score DESC")
    abstract fun allPostById(): DataSource.Factory<Int, Post>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(posts: List<Post>)

    @Transaction
    open fun insertToTop(posts: List<Post>) {
        deleteAllPosts()
        insert(posts)
    }

    @Query("DELETE FROM Post")
    abstract fun deleteAllPosts()

}