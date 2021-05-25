package com.example.shopmanagement

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration

import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class PostallyApplication: Application(), Configuration.Provider{

    @Inject lateinit var workerFactory : HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

}