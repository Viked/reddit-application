package com.test.redditapplication.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
abstract class TopPostsDao {

    @Query("SELECT * FROM Post ORDER BY score DESC")
    abstract fun allPostLiveData(): LiveData<List<Post>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(posts: List<Post>)

    @Transaction
    open fun insertToTop(posts: List<Post>) {
        deleteAllPosts()
        insert(posts)
    }

    @Query("DELETE FROM Post")
    abstract fun deleteAllPosts()

    @Query("SELECT * FROM Post ORDER BY score ASC LIMIT 1")
    abstract fun getLastPost(): Post

    @Query("SELECT * FROM Post WHERE id = :id LIMIT 1")
    abstract fun getPostById(id: String): Post

    @Query("SELECT * FROM Post WHERE localThumbnail = '' AND thumbnail != '' ORDER BY score DESC")
    abstract fun getPostToLoadImages(): List<Post>

}