package com.example.task_02

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import timber.log.Timber

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        ProcessLifecycleOwner.get().lifecycle.addObserver(BackgroundDetector())
    }
}