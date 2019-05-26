package com.test.redditapplication

import android.content.Context

private fun Context.getApp() = applicationContext as RedditApplication

fun Context.getDb() = getApp().db

fun Context.getPostDao() = getDb().topPostsDao()