package com.test.redditapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        WorkManager.getInstance()
            .beginUniqueWork(
                LOAD_LIST_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.from(LoadListWorker::class.java)
            ).enqueue()
    }
}
