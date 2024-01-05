package br.com.alura.forumapi.util

import java.time.LocalDateTime

object Clock {
    private var now: LocalDateTime? = null

    fun now(): LocalDateTime = now ?: LocalDateTime.now()

    fun setNowForTesting(date: LocalDateTime) {
        now = date
    }
}
