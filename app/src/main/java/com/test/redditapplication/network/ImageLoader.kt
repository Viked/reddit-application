package com.test.redditapplication.network

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.net.URL


interface ImageLoader {

    fun getFolder(): File

    fun getApplicationContext(): Context

    fun loadImage(imageUrl: String): String {
        val uri = Uri.parse(imageUrl)
        val fileName = uri.lastPathSegment ?: ""
        if (uri.authority.isNullOrBlank() || fileName.isBlank()) {
            return ""
        }

        val url = URL(imageUrl)
        val file = File(getApplicationContext().cacheDir, fileName)

        if (file.exists()) {
            return file.absolutePath
        }

        url.openConnection().getInputStream().use {
            val outputStream = file.outputStream()
            it.copyTo(outputStream)
            outputStream.close()
        }
        return file.absolutePath
    }

    fun addImageToGallery(filePath: String) {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.MediaColumns.DATA, filePath)
        getApplicationContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }

}