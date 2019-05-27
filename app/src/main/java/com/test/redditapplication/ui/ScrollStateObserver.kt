package com.test.redditapplication.ui

import android.content.SharedPreferences
import android.content.res.Configuration
import android.preference.PreferenceManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.test.redditapplication.SCROLL_STATE_KEY


class ScrollStateObserver(private val recyclerView: RecyclerView) : LifecycleObserver {

    private val layoutManager: StaggeredGridLayoutManager by lazy {
        val isLandscape = recyclerView.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val spanCount = if (isLandscape) 2 else 1
        StaggeredGridLayoutManager(spanCount, RecyclerView.VERTICAL)
    }

    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(recyclerView.context.applicationContext)
    }

    private val gson: Gson by lazy { Gson() }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        recyclerView.layoutManager = layoutManager
        val rawState = sharedPreferences.getString(SCROLL_STATE_KEY, null)
        if (rawState.isNullOrBlank()) return
        val state = gson.fromJson<StaggeredGridLayoutManager.SavedState>(rawState,
                StaggeredGridLayoutManager.SavedState::class.java)
        layoutManager.onRestoreInstanceState(state)
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_PAUSE)
    fun onDetach() {
        val state = layoutManager.onSaveInstanceState() ?: return
        sharedPreferences.edit().putString(SCROLL_STATE_KEY, gson.toJson(state)).apply()
    }

}