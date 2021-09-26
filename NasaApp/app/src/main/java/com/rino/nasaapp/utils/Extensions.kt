package com.rino.nasaapp.utils

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(
    @StringRes stringId: Int,
    duration: Int = Snackbar.LENGTH_SHORT,
    @StringRes actionStringId: Int? = null,
    action: View.OnClickListener? = null
) {
    val snackBar = Snackbar.make(this, stringId, duration)

    if (actionStringId != null && action != null) {
        snackBar.setAction(actionStringId, action)
    }

    snackBar.show()
}

fun View.showSnackBar(
    msg: String,
    duration: Int = Snackbar.LENGTH_SHORT,
    @StringRes actionStringId: Int? = null,
    action: View.OnClickListener? = null
) {
    val snackBar = Snackbar.make(this, msg, duration)

    if (actionStringId != null && action != null) {
        snackBar.setAction(actionStringId, action)
    }

    snackBar.show()
}

fun Context.searchInWikipedia(searchText: String) {
    val intent = Intent().apply {
        action = Intent.ACTION_VIEW
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        data = Uri.parse("https://en.wikipedia.org/wiki/$searchText")
    }

    startActivity(intent)
}

fun Context.showToast(@StringRes stringId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, stringId, duration).show()
}

fun Context.showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

fun Context.isDarkMode(): Boolean {
    return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_NO -> false // Night mode is not active, we're using the light theme
        Configuration.UI_MODE_NIGHT_YES -> true // Night mode is active, we're using dark theme
        else -> false
    }
}
