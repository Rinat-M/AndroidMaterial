package com.rino.nasaapp.ui.settings

import androidx.lifecycle.ViewModel
import com.rino.nasaapp.entities.Theme
import com.rino.nasaapp.wrappers.ThemeSharedPreferencesWrapper

class SettingsViewModel(
    private val themeSharedPreferencesWrapper: ThemeSharedPreferencesWrapper
) : ViewModel() {

    val currentTheme
        get() = themeSharedPreferencesWrapper.themeName

    fun saveSelectedTheme(theme: Theme?) {
        themeSharedPreferencesWrapper.themeName = theme
    }
}