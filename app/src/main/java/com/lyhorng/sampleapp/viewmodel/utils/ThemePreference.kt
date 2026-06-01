package com.lyhorng.sampleapp.viewmodel.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

object ThemePreference {
    private const val PREF_NAME = "theme_pref"
    private const val KEY_THEME_MODE = "theme_mode"

    const val MODE_SYSTEM = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    const val MODE_LIGHT = AppCompatDelegate.MODE_NIGHT_NO
    const val MODE_DARK = AppCompatDelegate.MODE_NIGHT_YES

    fun saveThemeMode(context: Context, mode: Int) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putInt(KEY_THEME_MODE, mode)
            .apply()
    }

    fun getThemeMode(context: Context): Int {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getInt(KEY_THEME_MODE, MODE_SYSTEM)
    }

    fun applyTheme(mode: Int) {
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}