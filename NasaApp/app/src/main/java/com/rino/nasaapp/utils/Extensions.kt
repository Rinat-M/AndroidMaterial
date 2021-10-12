package com.rino.nasaapp.utils

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Typeface.BOLD
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

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
    anchorViewId: Int? = null,
    @StringRes actionStringId: Int? = null,
    action: View.OnClickListener? = null
) {
    val snackBar = Snackbar.make(this, msg, duration)

    if (actionStringId != null && action != null) {
        snackBar.setAction(actionStringId, action)
    }

    anchorViewId?.let { snackBar.setAnchorView(it) }

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

fun Date.beginOfMonth(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1)
    return calendar.time
}

suspend fun ViewGroup.applyAnimation(
    transition: Transition,
    itemView: View,
    timeMillis: Long = 100
) {
    delay(timeMillis)
    TransitionManager.beginDelayedTransition(this, transition)
    itemView.isVisible = !itemView.isVisible
}

fun Date.toFormatString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

/**
 * Get all occurrences of a string
 * @return List of pairs with startIndex and endIndex of a string
 */
fun String.getAllOccurrencesOfString(query: String): List<Pair<Int, Int>> {
    val result = arrayListOf<Pair<Int, Int>>()

    var startIndex = 0
    var endIndex: Int

    do {
        startIndex = this.indexOf(query, startIndex, ignoreCase = true)

        if (startIndex == -1) {
            break
        }

        endIndex = this.indexOf(" ", startIndex, ignoreCase = true)

        if (endIndex == -1) {
            endIndex = this.length
        }

        result.add(Pair(startIndex, endIndex))

        startIndex = endIndex + 1
    } while (true)

    return result
}

/**
 * Get SpannableString with highlighted words
 * @param words - list of words to highlighting
 * @param color - color of highlighting
 * @param highlightInBold - highlight in bold
 * @return SpannableString with highlighted words in the specified color
 */
fun String.getSpannableWithHighlightedWords(
    words: List<String>,
    @ColorInt color: Int,
    highlightInBold: Boolean = true
): SpannableString {
    val listOfOccurrences = words.map { word ->
        this.getAllOccurrencesOfString(word)
    }.flatten()

    return SpannableString(this).apply {
        listOfOccurrences.forEach { pair ->
            this.setSpan(
                ForegroundColorSpan(color),
                pair.first,
                pair.second,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            if (highlightInBold) {
                this.setSpan(
                    StyleSpan(BOLD),
                    pair.first,
                    pair.second,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
    }
}