package com.test.redditapplication.network

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.test.redditapplication.db.Post
import com.test.redditapplication.db.TopPostsDao
import com.test.redditapplication.getPostDao
import java.io.File


class LoadImagesWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams), ImageLoader {

    private val postsDao: TopPostsDao by lazy { context.getPostDao() }

    override fun getFolder(): File = applicationContext.externalCacheDir
            ?: applicationContext.cacheDir

    override fun doWork(): Result {
        val posts = postsDao.getPostToLoadImages()
        posts.forEach {
            if (isStopped) return Result.failure()
            it.loadThumbnail()
        }
        return Result.success()
    }

    private fun Post.loadThumbnail() {
        val path = loadImage(thumbnail)

        val updatedPost = if (path.isBlank()) {
            copy(thumbnail = "")
        } else {
            copy(localThumbnail = path)
        }
        postsDao.insert(listOf(updatedPost))
    }


}