package com.example.myapplication.WrokManager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.myapplication.Data.Local.database.getDatabase
import com.example.myapplication.repos.Repository

class RefreshAsteroidsWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {
    companion object {
        const val WORK_NAME = "RefreshAsteroidsWorker"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = Repository(database)
        return if (repository.refreshAsteroids()) {
            repository.deletePreviousDayAsteroids()
            Result.success()
        } else {
            Result.retry()
        }
    }
}

