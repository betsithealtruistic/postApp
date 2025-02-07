package com.example.postapplication.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

object DarkModeManager {
    private const val PREFS_NAME = "dark_mode_prefs"
    private const val KEY_DARK_MODE = "is_dark_mode"

    fun toggleDarkMode(context: Context) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean(KEY_DARK_MODE, false)
        val newMode = if (isDarkMode) AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES

        AppCompatDelegate.setDefaultNightMode(newMode)
        prefs.edit().putBoolean(KEY_DARK_MODE, !isDarkMode).apply()
    }
}