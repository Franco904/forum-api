package br.com.alura.forumapi.domain.dto.answer

import br.com.alura.forumapi.domain.model.Answer
import br.com.alura.forumapi.util.formatter
import java.io.Serializable
import java.time.LocalDateTime

data class GetAnswerDto(
    val id: Long? = null,
    val message: String,
    val creationDate: LocalDateTime,
    val hasSolvedTopic: Boolean,
): Serializable {
    companion object {
        fun fromAnswer(answer: Answer): GetAnswerDto {
            return GetAnswerDto(
                id = answer.id,
                message = answer.message,
                creationDate = LocalDateTime.parse(answer.creationDate.format(formatter)),
                hasSolvedTopic = answer.hasSolvedTopic,
            )
        }
    }
}
