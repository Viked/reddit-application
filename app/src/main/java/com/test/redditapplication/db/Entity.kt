package com.test.redditapplication.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Post(
    @PrimaryKey val id: String,
    val author: String,
    val title: String,
    val thumbnail: String,
    val imageUrl: String,
    val url: String,
    val created: Date,
    val comments: Int,
    val score: Int
)