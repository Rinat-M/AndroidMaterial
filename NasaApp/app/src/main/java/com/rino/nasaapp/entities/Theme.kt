package com.rino.nasaapp.entities

import androidx.annotation.StyleRes
import com.rino.nasaapp.R

enum class Theme(@StyleRes val themeId: Int) {
    LIGHT(R.style.Theme_NasaApp),
    DARK(0),
    GREY(R.style.GreyTheme),
    BLUE_GREY(R.style.BlueGreyTheme),
    INDIGO(R.style.IndigoTheme),
    LIGHT_GREEN(R.style.LightGreenTheme),
    PURPLE(R.style.PurpleTheme)
}