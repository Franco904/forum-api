package br.com.alura.forumapi.domain.dto.error

import br.com.alura.forumapi.util.Clock
import java.time.LocalDateTime

data class GetErrorDto(
    val statusCode: Int,
    val timestamp: LocalDateTime = Clock.now(),
    val error: String,
    val message: String,
    val path: String,
)
