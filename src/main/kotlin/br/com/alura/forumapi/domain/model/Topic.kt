package br.com.alura.forumapi.domain.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = Topic.TABLE_NAME)
data class Topic(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val title: String,
    val message: String,
    val creationDate: LocalDateTime = LocalDateTime.now(),
    var updateDate: LocalDateTime = creationDate,
    @ManyToOne
    val course: Course,
    @ManyToOne
    val user: User,
    @Enumerated(value = EnumType.STRING)
    val status: StatusTopic = StatusTopic.NOT_ANSWERED,
    @OneToMany(mappedBy = "topic")
    val answers: List<Answer> = emptyList(),
) {
    fun copyWith(
        title: String? = null,
        message: String? = null,
        creationDate: LocalDateTime? = null,
        updateDate: LocalDateTime? = null,
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
            updateDate = updateDate ?: this.updateDate,
            course = course ?: this.course,
            user = user ?: this.user,
            status = status ?: this.status,
            answers = answers ?: this.answers,
        )
    }

    companion object {
        const val TABLE_NAME = "topics"
    }
}
