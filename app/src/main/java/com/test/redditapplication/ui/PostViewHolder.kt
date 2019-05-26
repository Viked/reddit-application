package com.test.redditapplication.ui

import androidx.recyclerview.widget.RecyclerView
import com.test.redditapplication.databinding.ItemPostBinding
import com.test.redditapplication.db.Post

class PostViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.post = post
    }

}