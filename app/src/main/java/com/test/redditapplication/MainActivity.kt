package com.test.redditapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.redditapplication.databinding.ActivityMainBinding
import com.test.redditapplication.ui.PostListAdapter

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, SimpleMainViewModelFactory(this))
            .get(MainViewModel::class.java)
    }

    private val adapter: PostListAdapter by lazy { PostListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.list.observe(this, Observer { adapter.postList(it ?: listOf()) })
        binding.rvPosts.adapter = adapter
        binding.rvPosts.layoutManager = LinearLayoutManager(this)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }
}
