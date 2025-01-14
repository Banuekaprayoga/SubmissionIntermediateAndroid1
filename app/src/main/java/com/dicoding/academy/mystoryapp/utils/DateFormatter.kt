package com.dicoding.academy.mystoryapp.utils

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object DateFormatter {
    fun formatDate(currentDate: String?): String? {
        val currentFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val targetFormat = "dd MMM yyyy"
        val timeZone = "GHT"
        val currentDf: DateFormat = SimpleDateFormat(currentFormat, Locale.getDefault())
        currentDf.timeZone = TimeZone.getTimeZone(timeZone)
        val targetDf: DateFormat = SimpleDateFormat(targetFormat, Locale.getDefault())
        var targetDate: String? = null
        try {
            val date = currentDf.parse(currentDate)
            if (date != null) {
                targetDate = targetDf.format(date)
            }
        } catch(ex: ParseException) {
            ex.printStackTrace()
        }
        return targetDate
    }
}