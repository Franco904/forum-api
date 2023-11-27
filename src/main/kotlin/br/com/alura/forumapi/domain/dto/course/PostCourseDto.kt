package br.com.alura.forumapi.domain.dto.course

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class PostCourseDto(
    @field:NotEmpty
    @field:Size(max = 50)
    val name: String,
    @field:NotEmpty val category: String,
)
