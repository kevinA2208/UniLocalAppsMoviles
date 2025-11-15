package com.example.appubicaciones

import android.app.Application
import com.cloudinary.android.MediaManager

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val config = mapOf(
            "cloud_name" to "ddiz4x8k7"
        )
        MediaManager.init(this, config)
    }
}