package com.rino.nasaapp.wrappers

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.rino.nasaapp.entities.Theme

class ThemeSharedPreferencesWrapper(context: Context) {

    companion object {
        private const val THEME_SETTINGS = "THEME_SETTINGS"
        private const val THEME_NAME = "THEME_NAME"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(THEME_SETTINGS, Context.MODE_PRIVATE)

    var themeName: Theme?
        get() = sharedPreferences.getString(THEME_NAME, null)?.let {
            Theme.valueOf(it)
        }
        set(value) {
            sharedPreferences.edit {
                putString(THEME_NAME, value?.name)
            }
        }

}