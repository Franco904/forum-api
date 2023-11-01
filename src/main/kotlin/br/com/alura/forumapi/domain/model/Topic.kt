package br.com.alura.forumapi.domain.model

import java.time.LocalDateTime

data class Topic(
    val id: Long? = null,
    val title: String,
    val message: String,
    val creationDate: LocalDateTime = LocalDateTime.now(),
    val course: Course,
    val user: User,
    val status: StatusTopic = StatusTopic.NOT_ANSWERED,
    val answers: List<Answer> = emptyList(),
) {
    fun copyWith(
        title: String? = null,
        message: String? = null,
        creationDate: LocalDateTime? = null,
        course: Course? = null,
        user: User? = null,
        status: StatusTopic? = null,
        answers: List<Answer>? = null,
    ): Topic {
        return Topic(
            id,
            title = title ?: this.title,
            message = message ?: this.message,
            creationDate = creationDate ?: this.creationDate,
            course = course ?: this.course,
            user = user ?: this.user,
            status = status ?: this.status,
            answers = answers ?: this.answers,
        )
    }
}
