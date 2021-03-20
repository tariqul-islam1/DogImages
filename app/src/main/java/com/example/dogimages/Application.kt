package com.example.dogimages

import android.app.Application
import com.dogimages.imgtest.ui.FontsOverride


class Application: Application() {
    override fun onCreate() {
        super.onCreate()
        FontsOverride.changeDefaultFont(this)
    }
}