package com.gianlucaparadise.castyourinstructions.application

import android.app.Application
import com.gianlucaparadise.castyourinstructions.cast.CastManager
import com.gianlucaparadise.castyourinstructions.notification.CastNotificationHandler

class MyApplication : Application() {

    lateinit var castNotificationHandler: CastNotificationHandler
    lateinit var castManager: CastManager

    override fun onCreate() {
        super.onCreate()
        instance = this

        this.castManager = CastManager(applicationContext)

        this.castNotificationHandler = CastNotificationHandler(applicationContext)
        this.castManager.addListener(this.castNotificationHandler)
    }

    companion object {
        lateinit var instance: MyApplication
            private set
    }
}