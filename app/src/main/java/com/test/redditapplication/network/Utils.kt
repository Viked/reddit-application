package com.test.redditapplication.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.work.*
import com.test.redditapplication.*
import com.test.redditapplication.db.Post

private fun createNetworkConstraints(): Constraints =
        Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

private inline fun <reified T : ListenableWorker> getNetworkRequest() =
        OneTimeWorkRequest.Builder(T::class.java)
                .setConstraints(createNetworkConstraints())
                .addTag(NETWORK_WORK_TAG)

fun getLoadingState(): LiveData<Boolean> {
    val workInfo = WorkManager.getInstance().getWorkInfosByTagLiveData(NETWORK_WORK_TAG)
    return Transformations.map(workInfo) { workList ->
        workList?.any { work -> work.state == WorkInfo.State.RUNNING } == true
    }
}

fun fetchTopPosts() {
    WorkManager.getInstance().cancelAllWorkByTag(NETWORK_WORK_TAG)
    WorkManager.getInstance().beginUniqueWork(
            LOAD_LIST_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            getNetworkRequest<FetchListWorker>().build()
    ).then(getNetworkRequest<LoadImagesWorker>().build()).enqueue()
}

fun loadNextPage() {
    WorkManager.getInstance().beginUniqueWork(
            LOAD_NEXT_PAGE_WORK_NAME,
            ExistingWorkPolicy.KEEP,
            getNetworkRequest<LoadNextPageWorker>().build()
    ).then(getNetworkRequest<LoadImagesWorker>().build()).enqueue()
}

fun loadImage(post: Post) {
    val data = Data.Builder().putString(POST_ID_KEY, post.id).build()
    WorkManager.getInstance().beginUniqueWork(
            LOAD_IMAGE_WORK_NAME,
            ExistingWorkPolicy.KEEP,
            getNetworkRequest<LoadImageWorker>().setInputData(data).build()
    ).enqueue()
}