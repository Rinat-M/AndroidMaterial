package com.rino.nasaapp.entities

import java.text.SimpleDateFormat
import java.util.*

enum class DateParameter {
    TODAY,
    YESTERDAY,
    DAY_BEFORE_YESTERDAY;

    fun toDateString(): String {
        val calendar = Calendar.getInstance()
        calendar.time = Date()

        when (this) {
            TODAY -> calendar.time
            YESTERDAY -> calendar.add(Calendar.DATE, -1)
            DAY_BEFORE_YESTERDAY -> calendar.add(Calendar.DATE, -2)
        }

        val date = calendar.time
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        return formatter.format(date)
    }
}