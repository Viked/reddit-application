package com.test.redditapplication.network

import android.content.Context
import android.net.Uri
import android.text.Html
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.test.redditapplication.REDDIT
import com.test.redditapplication.SCHEME
import com.test.redditapplication.TOP_POSTS_PATH
import com.test.redditapplication.db.Post
import com.test.redditapplication.db.TopPostsDao
import com.test.redditapplication.getPostDao
import java.net.URL
import java.util.*
import kotlin.math.roundToLong

abstract class BaseRedditNetworkWorker(context: Context, workerParams: WorkerParameters) :
        Worker(context, workerParams) {

    abstract fun insert(dao: TopPostsDao, list: List<Post>)

    val postsDao: TopPostsDao by lazy { context.getPostDao() }

    open fun getRequestBuilder(): Uri.Builder = Uri.Builder().scheme(SCHEME)
            .authority(REDDIT)
            .appendPath(TOP_POSTS_PATH)

    private fun parseResponse(response: String): TopResponse = Gson().fromJson(response, TopResponse::class.java)

    private fun TopResponse.mapToDbEntity() = data?.children?.map {
        Post(
                id = it.data?.name ?: "",
                author = it.data?.author ?: "",
                title = it.data?.title ?: "",
                thumbnail = it.data?.thumbnail.decodeUrl(),
                url = "$SCHEME://$REDDIT${it.data?.permalink}",
                created = Date((it.data?.created_utc?.roundToLong() ?: 0) * 1000),
                comments = it.data?.num_comments ?: 0,
                score = it.data?.score ?: 0,
                imageUrl = it.data?.preview?.images?.firstOrNull()?.source?.url.decodeUrl(),
                localThumbnail = ""
        )
    } ?: listOf()

    private fun String?.decodeUrl(): String = if (!isNullOrBlank()) {
        Html.fromHtml(this).toString()
    } else ""

    override fun doWork(): Result {
        val request = getRequestBuilder().build().toString()
        val url = URL(request)

        val rawResponse = try {
            url.readText()
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "Loading error", e)
            return Result.failure()
        }

        if (rawResponse.isBlank()) return Result.failure()

        val posts = parseResponse(rawResponse).mapToDbEntity()
        insert(postsDao, posts)
        return Result.success()
    }

}