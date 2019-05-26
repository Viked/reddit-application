package com.test.redditapplication.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.test.redditapplication.databinding.ItemPostBinding
import com.test.redditapplication.db.Post

class PostListAdapter : RecyclerView.Adapter<PostViewHolder>() {

    private val differ: AsyncListDiffer<Post> by lazy { AsyncListDiffer(this, PostDifferCallback()) }

    fun postList(list: List<Post>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PostViewHolder(
        ItemPostBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }
}