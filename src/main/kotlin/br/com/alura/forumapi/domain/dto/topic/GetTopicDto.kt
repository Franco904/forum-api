package br.com.alura.forumapi.domain.dto.topic

import br.com.alura.forumapi.domain.model.StatusTopic
import br.com.alura.forumapi.domain.model.Topic
import java.time.LocalDateTime

data class GetTopicDto(
    val id: Long? = null,
    val title: String,
    val message: String,
    val status: StatusTopic,
    val creationDate: LocalDateTime,
    val updateDate: LocalDateTime,
) {
    companion object {
        fun fromTopic(topic: Topic): GetTopicDto {
            return GetTopicDto(
                id = topic.id,
                title = topic.title,
                message = topic.message,
                status = topic.status,
                creationDate = topic.creationDate,
                updateDate = topic.updateDate,
            )
        }
    }
}
