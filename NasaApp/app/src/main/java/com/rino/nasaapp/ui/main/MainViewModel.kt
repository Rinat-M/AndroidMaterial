package com.rino.nasaapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rino.nasaapp.entities.Screen
import com.rino.nasaapp.wrappers.ThemeSharedPreferencesWrapper

class MainViewModel(
    private val themeSharedPreferencesWrapper: ThemeSharedPreferencesWrapper
) : ViewModel() {

    val currentTheme
        get() = themeSharedPreferencesWrapper.themeName

    private val _screen: MutableLiveData<Screen> = MutableLiveData(Screen.MAIN)
    val screen: LiveData<Screen> = _screen

    fun changeScreen(changedScreen: Screen) {
        _screen.value = changedScreen
    }
}
