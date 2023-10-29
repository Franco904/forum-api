package br.com.alura.forumapi.domain.dto.topic

import br.com.alura.forumapi.domain.model.StatusTopic
import br.com.alura.forumapi.domain.model.Topic
import java.time.LocalDateTime

data class TopicOutputDto(
    val id: Long? = null,
    val title: String,
    val message: String,
    val status: StatusTopic,
    val creationDate: LocalDateTime,
) {
    companion object {
        fun fromTopic(topic: Topic): TopicOutputDto {
            return TopicOutputDto(
                id = topic.id,
                title = topic.title,
                message = topic.message,
                status = topic.status,
                creationDate = topic.creationDate,
            )
        }
    }
}
