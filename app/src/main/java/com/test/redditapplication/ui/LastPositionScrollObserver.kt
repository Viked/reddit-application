package com.test.redditapplication.ui

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import com.test.redditapplication.SCROLL_DIRECTION

class LastPositionScrollObserver(private val recyclerView: RecyclerView, private val onScrolledToBottom: () -> Unit) :
    RecyclerView.OnScrollListener(), LifecycleObserver {

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_RESUME)
    fun onAttach() {
        recyclerView.addOnScrollListener(this)
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_PAUSE)
    fun onDetach() {
        recyclerView.removeOnScrollListener(this)
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (!recyclerView.canScrollVertically(SCROLL_DIRECTION)) {
            onScrolledToBottom()
        }
    }
}