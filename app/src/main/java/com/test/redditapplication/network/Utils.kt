package com.test.redditapplication.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.work.*
import com.test.redditapplication.LAST_ID
import com.test.redditapplication.LOAD_LIST_WORK_NAME

private fun createNetworkConstraints(): Constraints =
    Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

private fun createRequestBuilder() = OneTimeWorkRequest.Builder(LoadListWorker::class.java)
    .setConstraints(createNetworkConstraints())


fun getLoadingState(): LiveData<Boolean> {
    val workInfo = WorkManager.getInstance().getWorkInfosForUniqueWorkLiveData(LOAD_LIST_WORK_NAME)
    return Transformations.map(workInfo) { workList ->
        workList?.any { work -> work.state == WorkInfo.State.RUNNING } == true
    }
}

fun fetchTopPosts() {
    WorkManager.getInstance().beginUniqueWork(
        LOAD_LIST_WORK_NAME,
        ExistingWorkPolicy.REPLACE,
        createRequestBuilder().build()
    ).enqueue()
}

fun loadNextPage(id: String) {
    val data = Data.Builder().putString(LAST_ID, id).build()
    WorkManager.getInstance().beginUniqueWork(
        LOAD_LIST_WORK_NAME,
        ExistingWorkPolicy.REPLACE,
        createRequestBuilder()
            .setInputData(data)
            .build()
    ).enqueue()
}