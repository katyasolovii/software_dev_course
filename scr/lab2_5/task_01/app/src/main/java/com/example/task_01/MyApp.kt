package com.example.task_01

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import timber.log.Timber

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                val state = (activity as LifecycleOwner).lifecycle.currentState
                Timber.d("AppCallbacks: onActivityCreated -> ${activity.javaClass.simpleName}, стан: $state")
            }

            override fun onActivityStarted(activity: Activity) {
                val state = (activity as LifecycleOwner).lifecycle.currentState
                Timber.d("AppCallbacks: onActivityStarted -> ${activity.javaClass.simpleName}, стан: $state")
            }

            override fun onActivityResumed(activity: Activity) {
                val state = (activity as LifecycleOwner).lifecycle.currentState
                Timber.d("AppCallbacks: onActivityResumed -> ${activity.javaClass.simpleName}, стан: $state")
            }

            override fun onActivityPaused(activity: Activity) {
                val state = (activity as LifecycleOwner).lifecycle.currentState
                Timber.d("AppCallbacks: onActivityPaused -> ${activity.javaClass.simpleName}, стан: $state")
            }

            override fun onActivityStopped(activity: Activity) {
                val state = (activity as LifecycleOwner).lifecycle.currentState
                Timber.d("AppCallbacks: onActivityStopped -> ${activity.javaClass.simpleName}, стан: $state")
            }

            override fun onActivityDestroyed(activity: Activity) {
                val state = (activity as LifecycleOwner).lifecycle.currentState
                Timber.d("AppCallbacks: onActivityDestroyed -> ${activity.javaClass.simpleName}, стан: $state")
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
        })
    }
}