package br.com.alura.forumapi.domain.model

import java.time.LocalDateTime

data class Answer(
    val id: Long? = null,
    val message: String,
    val creationDate: LocalDateTime = LocalDateTime.now(),
    val user: User,
    val hasSolvedTopic: Boolean = false,
)
