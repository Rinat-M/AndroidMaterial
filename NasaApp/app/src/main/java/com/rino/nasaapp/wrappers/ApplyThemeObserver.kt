package com.rino.nasaapp.wrappers

import android.content.res.Resources

interface ApplyThemeObserver {
    fun applyTheme()
    fun applyThemeNow()
    fun getCurrentTheme(): Resources.Theme?
}