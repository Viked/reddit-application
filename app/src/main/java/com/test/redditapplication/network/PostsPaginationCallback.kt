package com.test.redditapplication.network

import androidx.paging.PagedList
import com.test.redditapplication.db.Post

class PostsPaginationCallback : PagedList.BoundaryCallback<Post>() {

    override fun onZeroItemsLoaded() {
        fetchTopPosts()
    }

    override fun onItemAtEndLoaded(itemAtEnd: Post) {
//        loadNextPage(itemAtEnd.id)
    }
}