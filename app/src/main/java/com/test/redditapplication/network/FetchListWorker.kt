package com.test.redditapplication.network

import android.content.Context
import androidx.work.WorkerParameters
import com.test.redditapplication.db.Post
import com.test.redditapplication.db.TopPostsDao

class FetchListWorker(context: Context, workerParams: WorkerParameters) :
    BaseRedditNetworkWorker(context, workerParams) {
    override fun insert(dao: TopPostsDao, list: List<Post>) {
        dao.insertToTop(list)
    }
}