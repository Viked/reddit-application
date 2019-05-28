package com.test.redditapplication

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.test.redditapplication.databinding.ActivityMainBinding
import com.test.redditapplication.db.Post
import com.test.redditapplication.network.loadImage
import com.test.redditapplication.ui.LastPositionScrollObserver
import com.test.redditapplication.ui.PostListAdapter
import com.test.redditapplication.ui.ScrollStateObserver


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, SimpleMainViewModelFactory(this))
                .get(MainViewModel::class.java)
    }

    private val adapter: PostListAdapter by lazy { PostListAdapter(this::openPost, this::openImage, ::loadImage) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.list.observe(this, Observer { setList(it) })
        binding.rvPosts.adapter = adapter
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        LastPositionScrollObserver(binding.rvPosts) {
            loadListIfCan()
        }.setLifecycleOwner(this)

    }

    private fun openImage(post: Post) {
        if (post.imageUrl.isNotBlank()) openUrl(post.imageUrl)
    }

    private fun openPost(post: Post) = openUrl(post.url)

    private fun openUrl(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    private fun setList(list: List<Post>?) {
        list ?: return
        if (list.isEmpty()) {
            loadListIfCan()
            return
        }

        val wasEmpty = adapter.itemCount == 0
        adapter.postList(list)
        if (wasEmpty) {
            ScrollStateObserver(binding.rvPosts).setLifecycleOwner(this)
        }
    }

    private fun loadListIfCan() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            viewModel.onLoadNextPage()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == WRITE_PERMISSION_CODE && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            loadListIfCan()
        }
    }

}
