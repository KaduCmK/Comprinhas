package com.example.comprinhas.core.data

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class TimeDiff {

    companion object {
        fun calculateTimeDiff(date: Long): String {
            val secDiff = ZonedDateTime.now().toEpochSecond() - date
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
                Instant.ofEpochSecond(date)
                    .atZone(ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            }
        }
    }
}