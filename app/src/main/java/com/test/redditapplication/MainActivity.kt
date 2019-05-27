package com.test.redditapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.redditapplication.databinding.ActivityMainBinding
import com.test.redditapplication.db.Post
import com.test.redditapplication.ui.LastPositionScrollObserver
import com.test.redditapplication.ui.PostListAdapter


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, SimpleMainViewModelFactory(this))
                .get(MainViewModel::class.java)
    }

    private val adapter: PostListAdapter by lazy { PostListAdapter(this::openPost, this::openImage) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.list.observe(this, Observer { adapter.postList(it ?: listOf()) })
        binding.rvPosts.adapter = adapter
        binding.rvPosts.layoutManager = LinearLayoutManager(this)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        LastPositionScrollObserver(binding.rvPosts) {
            viewModel.onLoadNextPage()
        }.setLifecycleOwner(this)
    }

    private fun openImage(post: Post) {
        if (post.imageUrl.isNotBlank()) openUrl(post.imageUrl)
    }

    private fun openPost(post: Post) = openUrl(post.url)

    private fun openUrl(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

}
