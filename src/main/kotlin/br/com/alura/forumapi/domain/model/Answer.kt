package br.com.alura.forumapi.domain.model

import br.com.alura.forumapi.util.Clock
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = Answer.TABLE_NAME)
data class Answer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val message: String,
    val creationDate: LocalDateTime = Clock.now(),
    @ManyToOne
    val user: User,
    @ManyToOne
    val topic: Topic,
    val hasSolvedTopic: Boolean = false,
) {
    companion object {
        const val TABLE_NAME = "answers"
    }
}
