package br.com.alura.forumapi.util

import java.time.LocalDateTime

object Clock {
    private var now: LocalDateTime? = null

    fun now(): LocalDateTime {
        return now ?: LocalDateTime.parse(LocalDateTime.now().format(formatter))
    }

    fun setNowForTesting(date: LocalDateTime) {
        now = LocalDateTime.parse(date.format(formatter))
    }
}
