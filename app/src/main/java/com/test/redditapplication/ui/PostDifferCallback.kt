package com.test.redditapplication.ui

import androidx.recyclerview.widget.DiffUtil
import com.test.redditapplication.db.Post

class PostDifferCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem
}