package com.test.redditapplication.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.work.*
import com.test.redditapplication.LOAD_LIST_WORK_NAME
import com.test.redditapplication.LOAD_NEXT_PAGE_WORK_NAME
import com.test.redditapplication.NETWORK_WORK_TAG

private fun createNetworkConstraints(): Constraints =
    Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

private inline fun <reified T : ListenableWorker> getNetworkRequest() =
    OneTimeWorkRequest.Builder(T::class.java)
        .setConstraints(createNetworkConstraints())
        .addTag(NETWORK_WORK_TAG)
        .build()

fun getLoadingState(): LiveData<Boolean> {
    val workInfo = WorkManager.getInstance().getWorkInfosByTagLiveData(NETWORK_WORK_TAG)
    return Transformations.map(workInfo) { workList ->
        workList?.any { work -> work.state == WorkInfo.State.RUNNING } == true
    }
}

fun fetchTopPosts() {
    WorkManager.getInstance().beginUniqueWork(
        LOAD_LIST_WORK_NAME,
        ExistingWorkPolicy.REPLACE,
        getNetworkRequest<FetchListWorker>()
    ).enqueue()
}

fun loadNextPage() {
    WorkManager.getInstance().beginUniqueWork(
        LOAD_NEXT_PAGE_WORK_NAME,
        ExistingWorkPolicy.KEEP,
        getNetworkRequest<LoadNextPageWorker>()
    ).enqueue()
}