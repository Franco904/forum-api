package br.com.alura.forumapi.domain.dto.error

import java.time.LocalDateTime

data class GetErrorDto(
    val statusCode: Int,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val error: String,
    val message: String,
    val path: String,
)
