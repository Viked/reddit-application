package com.test.redditapplication.network

import android.content.Context
import android.provider.MediaStore
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.test.redditapplication.POST_ID_KEY
import com.test.redditapplication.db.TopPostsDao
import com.test.redditapplication.getPostDao
import java.io.File


class LoadImageWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams), ImageLoader {

    private val postsDao: TopPostsDao by lazy { context.getPostDao() }

    override fun getFolder(): File = applicationContext.cacheDir

    override fun doWork(): Result {
        val id = inputData.getString(POST_ID_KEY) ?: return Result.failure()
        val post = postsDao.getPostById(id)
        val path = loadImage(post.imageUrl)

        return if (path.isNotBlank()) {
            MediaStore.Images.Media.insertImage(applicationContext.contentResolver, path, post.title, post.title)
            postsDao.insert(listOf(post.copy(savedToMedia = true)))
            File(path).delete()
            Result.success()
        } else {
            Result.failure()
        }
    }

}