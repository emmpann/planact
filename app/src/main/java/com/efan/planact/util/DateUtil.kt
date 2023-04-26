package com.efan.planact.util

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object DateUtil {

    private val dateFormat: SimpleDateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.US)

    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy")

    private val current = LocalDate.now().format(formatter)

    fun getCurrentDate(): String {
        return current
    }

    fun formattedDate(year: Int, month: Int, dayOfMonth: Int): String {
        return LocalDate.of(year, month+1, dayOfMonth).format(formatter)
    }

    fun getTimeInMillis(dateAndTime: String): Long {
        val date = dateFormat.parse(dateAndTime)

        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.timeInMillis
    }
}