package com.test.redditapplication

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

private fun Context.getApp() = applicationContext as RedditApplication

fun Context.getDb() = getApp().db

fun Context.getPostDao() = getDb().topPostsDao()

class SimpleMainViewModelFactory(private val activity: Activity) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass == MainViewModel::class.java) {
            return MainViewModel(activity.getPostDao()) as T
        }
        throw ClassCastException("$modelClass is not valid view model class")
    }
}