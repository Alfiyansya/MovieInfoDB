package com.alfiansyah.movieinfodb.utils

import java.text.SimpleDateFormat
import java.util.*

@Suppress("unused", "MemberVisibilityCanBePrivate")
object DateFormatterHelper {
    var datePattern = "yyyy-MM-dd"
    private val dateFormat get() = SimpleDateFormat(datePattern, Locale.getDefault())

    fun format(date: Date) : String {
        return dateFormat.format(date)
    }

    fun toCalendarInstance(dateInString: String?): Calendar {
        val calendar = Calendar.getInstance()

        dateInString?.let {
            val date = dateFormat.parse(it)
            if (date != null) calendar.time = date
        }

        return calendar
    }

    fun toDateInstance(dateInString: String?): Date {
        return toCalendarInstance(dateInString).time
    }

}