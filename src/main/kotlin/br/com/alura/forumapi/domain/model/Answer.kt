package br.com.alura.forumapi.domain.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "answers")
data class Answer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val message: String,
    val creationDate: LocalDateTime = LocalDateTime.now(),
    @ManyToOne
    val user: User,
    @ManyToOne
    val topic: Topic,
    val hasSolvedTopic: Boolean = false,
)
