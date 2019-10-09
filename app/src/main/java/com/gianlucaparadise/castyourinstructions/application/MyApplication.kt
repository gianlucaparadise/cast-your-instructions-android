package com.gianlucaparadise.castyourinstructions.application

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.gianlucaparadise.castyourinstructions.notification.CastNotificationHandler

class MyApplication : Application() {

    lateinit var castNotificationHandler: CastNotificationHandler

    override fun onCreate() {
        super.onCreate()
        instance = this

        this.castNotificationHandler = CastNotificationHandler();
        ProcessLifecycleOwner.get().lifecycle.addObserver(this.castNotificationHandler)
    }

    companion object {
        lateinit var instance: MyApplication
            private set
    }
}