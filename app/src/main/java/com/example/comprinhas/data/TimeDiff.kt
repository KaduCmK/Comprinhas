package com.example.comprinhas.data

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class TimeDiff {

    companion object {
        fun calculateTimeDiff(date: ZonedDateTime): String {
            val secDiff = ZonedDateTime.now().toEpochSecond() - date.toEpochSecond()
            return if (secDiff < 60) {
                "agora"
            }
            else if (secDiff < 3600) {
                "há ${secDiff / 60}min"
            }
            else if (secDiff < 86400) {
                "há ${secDiff / 3600}h"
            }
            else {
                date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            }
        }
    }
}