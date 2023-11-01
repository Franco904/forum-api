package br.com.alura.forumapi.domain.dto.topic

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class PutTopicDto(
    @field:NotNull val id: Long,
    @field:NotEmpty
    @field:Size(min = 5, max = 100)
    val title: String,
    @field:NotEmpty val message: String,
)
