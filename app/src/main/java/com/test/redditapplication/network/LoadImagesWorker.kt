package com.test.redditapplication.network

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.test.redditapplication.db.Post
import com.test.redditapplication.db.TopPostsDao
import com.test.redditapplication.getPostDao
import java.io.File
import java.net.URL


class LoadImagesWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private val postsDao: TopPostsDao by lazy { context.getPostDao() }

    override fun doWork(): Result {
        val posts = postsDao.getPostToLoadImages()

        posts.forEach {
            if (isStopped) return Result.failure()
            it.loadImage()
        }
        return Result.success()
    }

    private fun Post.loadImage() {
        val uri = Uri.parse(thumbnail)
        val fileName = uri.lastPathSegment ?: ""
        if (uri.authority.isNullOrBlank() || fileName.isBlank()) {
            postsDao.insert(listOf(copy(thumbnail = "")))
            return
        }

        Log.d(javaClass.simpleName, thumbnail)

        val url = URL(thumbnail)
        val file = File(applicationContext.cacheDir, fileName)
        url.openConnection().getInputStream().use {
            val outputStream = file.outputStream()
            it.copyTo(outputStream)
            outputStream.close()
        }
        addImageToGallery(file.absolutePath)
        postsDao.insert(listOf(copy(localThumbnail = file.absolutePath)))
    }

    private fun addImageToGallery(filePath: String) {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.MediaColumns.DATA, filePath)
        applicationContext.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }

}