package br.com.alura.forumapi.domain.dto.topic

data class GetTopicsByCategoryDto(
    val category: String,
    val topics: List<GetTopicDto>,
)
