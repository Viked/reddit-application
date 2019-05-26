package com.test.redditapplication

import androidx.multidex.MultiDexApplication
import com.test.redditapplication.db.RedditDatabase

class RedditApplication : MultiDexApplication() {

    val db: RedditDatabase by lazy { RedditDatabase.get(this) }

}