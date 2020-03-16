package com.eosr14.masksearch.common

import java.text.SimpleDateFormat
import java.util.*

fun convertDisplayDate(dateTime: String): String {
    return if (dateTime.isEmpty()) {
        ""
    } else {
        val isoDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault()).parse(dateTime)
        return SimpleDateFormat("yyyy.MM.dd. HH:mm", Locale.getDefault()).format(isoDate)
    }
}