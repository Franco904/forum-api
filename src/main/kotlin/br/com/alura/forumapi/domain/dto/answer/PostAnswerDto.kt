package br.com.alura.forumapi.domain.dto.answer

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

class PostAnswerDto(
    @field:NotEmpty val message: String,
    @field:NotNull val userId: Long,
)
