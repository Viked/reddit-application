package com.test.redditapplication.network

import android.content.Context
import androidx.work.WorkerParameters
import com.test.redditapplication.db.Post
import com.test.redditapplication.db.TopPostsDao

class LoadNextPageWorker(context: Context, workerParams: WorkerParameters) :
    BaseRedditNetworkWorker(context, workerParams) {

    private fun getLastId(): String = postsDao.getLastPost().id

    override fun getRequestBuilder() = super.getRequestBuilder().appendQueryParameter("after", getLastId())

    override fun insert(dao: TopPostsDao, list: List<Post>) {
        dao.insert(list)
    }
}