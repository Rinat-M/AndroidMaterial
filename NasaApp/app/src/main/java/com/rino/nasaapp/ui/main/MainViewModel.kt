package com.rino.nasaapp.ui.main

import androidx.lifecycle.ViewModel
import com.rino.nasaapp.entities.Theme
import com.rino.nasaapp.wrappers.ThemeSharedPreferencesWrapper

class MainViewModel(
    private val themeSharedPreferencesWrapper: ThemeSharedPreferencesWrapper
) : ViewModel() {

    val currentTheme
        get() = themeSharedPreferencesWrapper.themeName

    fun changeTheme(theme: Theme?) {
        themeSharedPreferencesWrapper.themeName = theme
    }
}