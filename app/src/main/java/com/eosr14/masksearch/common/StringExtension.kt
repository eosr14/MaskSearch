package com.eosr14.masksearch.common

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String.convertDisplayDate(): String {
    return if (this.isEmpty()) {
        ""
    } else {
        try {
            val isoDate = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).parse(this)
            return "갱신시간 : ${SimpleDateFormat(
                "yyyy.MM.dd. HH:mm",
                Locale.getDefault()
            ).format(isoDate)}"
        } catch (e: ParseException) {
            ""
        }
    }
}

fun String.convertMaskCount(): String {
    return if (this.isEmpty()) {
        ""
    } else {
        var message = "마스크 수량 : "
        message += when (this) {
            "plenty" -> "100개 이상"
            "some" -> "30개 이상"
            "few" -> "30개 미만"
            "empty", "break" -> "품절"
            else -> "품절"
        }
        return message
    }
}