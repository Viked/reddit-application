package com.test.redditapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.test.redditapplication.db.Post
import com.test.redditapplication.db.TopPostsDao
import com.test.redditapplication.network.fetchTopPosts
import com.test.redditapplication.network.getLoadingState
import com.test.redditapplication.network.loadNextPage

class MainViewModel(private val postsDao: TopPostsDao) : ViewModel() {

    val isLoading: LiveData<Boolean> by lazy { getLoadingState() }

    val list: LiveData<List<Post>> by lazy { postsDao.allPostLiveData() }

    fun onRefresh() {
        fetchTopPosts()
    }

    fun onLoadNextPage() {
        if (isLoading.value == true) return
        if (list.value?.isEmpty() == true) {
            fetchTopPosts()
        } else {
            loadNextPage()
        }
    }

}