package com.test.redditapplication.network

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.test.redditapplication.LAST_ID
import com.test.redditapplication.db.Post
import com.test.redditapplication.db.TopPostsDao
import com.test.redditapplication.getPostDao
import java.net.URL
import java.util.*
import kotlin.math.roundToLong

class LoadListWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private val lastId: String by lazy {
        workerParams.inputData.getString(LAST_ID) ?: ""
    }

    private val postsDao: TopPostsDao by lazy { context.getPostDao() }

    private fun getRequestUrl(): String {
        val builder = Uri.Builder().scheme("https")
            .authority("www.reddit.com")
            .appendPath("top.json")

        if (lastId.isNotBlank()) {
            builder.appendQueryParameter("after", lastId)
        }

        return builder.build().toString()
    }

    private fun parseResponse(response: String): TopResponse = Gson().fromJson(response, TopResponse::class.java)

    private fun TopResponse.mapToDbEntity() = data?.children?.map {
        Post(
            id = it.data?.name ?: "",
            author = it.data?.author ?: "",
            title = it.data?.title ?: "",
            thumbnail = it.data?.thumbnail ?: "",
            url = it.data?.url ?: "",
            created = Date(it.data?.created_utc?.roundToLong() ?: 0),
            comments = it.data?.num_comments ?: 0,
            viewCount = it.data?.view_count ?: 0,
            score = it.data?.score ?: 0,
            imageUrl = it.data?.preview?.images?.firstOrNull()?.source?.url ?: ""
        )
    } ?: listOf()

    override fun doWork(): Result {
        val request = getRequestUrl()
        val url = URL(request)

        val rawResponse = try {
            url.readText()
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "Loading error", e)
            return Result.failure()
        }

        if (rawResponse.isBlank()) return Result.failure()

        val posts = parseResponse(rawResponse).mapToDbEntity()
        if (lastId.isNotBlank()) {
            postsDao.insert(posts)
        } else {
            postsDao.insertToTop(posts)
        }
        return Result.success()
    }

}