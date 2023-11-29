package br.com.alura.forumapi.domain.dto.topic

data class GetTopicCountByCategoryDto(
    val category: String,
    val topicCount: Long,
) {
    companion object {
        const val PATH = "br.com.alura.forumapi.domain.dto.topic.GetTopicCountByCategoryDto"
    }
}
