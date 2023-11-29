package br.com.alura.forumapi.domain.dto.topic

data class GetCategoryAndTopicDto(
    val category: String,
    val topic: GetTopicDto,
) {
    companion object {
        const val PATH = "br.com.alura.forumapi.domain.dto.topic.GetCategoryAndTopicDto"
    }
}
