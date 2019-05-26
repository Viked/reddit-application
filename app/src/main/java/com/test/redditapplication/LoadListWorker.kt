package com.test.redditapplication

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.test.redditapplication.db.TopResponse
import java.net.URL

class LoadListWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private fun getRequestUrl(): String {
        val builder = Uri.Builder().scheme("https")
            .authority("www.reddit.com")
            .appendPath("top.json")

        if (false) {
            builder.appendQueryParameter("key", "[redacted]")
        }

        return builder.build().toString()
    }

    private fun parseResponse(response: String): TopResponse = Gson().fromJson(response, TopResponse::class.java)

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

        val response = parseResponse(rawResponse)
        Log.d(javaClass.simpleName, "Reddit response: $response")
        return Result.success()
    }


}