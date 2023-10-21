package br.com.alura.forumapi.service

import br.com.alura.forumapi.domain.model.Answer
import br.com.alura.forumapi.domain.model.Course
import br.com.alura.forumapi.domain.model.Topic
import br.com.alura.forumapi.domain.model.User
import org.springframework.stereotype.Service

@Service
class TopicService {
    private var topics: List<Topic> = emptyList()

    init {
        topics = listOf(
            Topic(
                id = 1,
                title = "Dúvida com declaração de variável em Kotlin",
                message = "Como declaro variáveis de tipo Enum no Kotlin?",
                course = Course(
                    id = 1,
                    name = "Kotlin",
                    category = "Programming",
                ),
                user = User(
                    id = 1,
                    name = "little_grasshopper99",
                    email = "lgr@email.com",
                ),
                answers = listOf(
                    Answer(
                        id = 1,
                        message = "É simples! Voce pode usar o val e o var",
                        user = User(
                            id = 2,
                            name = "bigoneakz",
                            email = "bgglk@email.com",
                        ),
                    )
                )
            )
        )
    }

    fun findAll(): List<Topic> = topics

    fun findById(id: Long): Topic? = topics.find { it.id == id }

    fun findAnswers(id: Long): List<Answer> = topics.find { it.id == id }?.answers ?: emptyList()
}
