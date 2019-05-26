package com.test.redditapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.test.redditapplication.db.Post
import com.test.redditapplication.db.TopPostsDao
import com.test.redditapplication.network.PostsPaginationCallback
import com.test.redditapplication.network.getLoadingState

class MainViewModel(private val postsDao: TopPostsDao) : ViewModel() {

    val loading: LiveData<Boolean> = getLoadingState()

    val list: LiveData<PagedList<Post>> by lazy {
        postsDao.allPostById().toLiveData(PAGE_SIZE, boundaryCallback = PostsPaginationCallback())
    }

}