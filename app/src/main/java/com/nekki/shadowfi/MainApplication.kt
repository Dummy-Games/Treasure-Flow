package com.nekki.shadowfi

import android.app.Application
import com.onesignal.OneSignal

/**
 * Created by Android Studio on 20.02.2022 21:58
 *
 * @author Vlad Makarenko
 */

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        OneSignal.initWithContext(applicationContext)
        OneSignal.setAppId("9ac15b77-7461-4211-b37d-62ccd76fc866")
    }
}