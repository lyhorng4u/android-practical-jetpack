package com.lyhorng.sampleapp

import android.app.Application
import com.lyhorng.sampleapp.data.remote.BaseService
import com.lyhorng.sampleapp.viewmodel.utils.PreferenceManager
import com.lyhorng.sampleapp.viewmodel.utils.ThemePreference

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        ThemePreference.applyTheme(ThemePreference.getThemeMode(this))
        val preferenceManager = PreferenceManager(this)
        BaseService.initialize(preferenceManager)
    }
}