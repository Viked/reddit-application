package com.test.redditapplication.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.test.redditapplication.databinding.ItemPostBinding
import com.test.redditapplication.db.Post

typealias OpenLink = (Post) -> Unit

class PostListAdapter(private val openPost: OpenLink, private val openImage: OpenLink) : RecyclerView.Adapter<PostViewHolder>() {

    val differ: AsyncListDiffer<Post> by lazy { AsyncListDiffer(this, PostDifferCallback()) }

    fun postList(list: List<Post>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PostViewHolder(
            ItemPostBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
            ).apply {
                ivThumbnail.setOnClickListener { openImage(post ?: return@setOnClickListener) }
                bOpenUrl.setOnClickListener { openPost(post ?: return@setOnClickListener) }
            }
    )

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }
}