package br.com.alura.forumapi.service

import br.com.alura.forumapi.domain.dto.topic.TopicGetDto
import br.com.alura.forumapi.domain.dto.topic.TopicPostDto
import br.com.alura.forumapi.domain.dto.topic.TopicPutDto
import br.com.alura.forumapi.domain.model.Answer
import br.com.alura.forumapi.domain.model.Topic
import br.com.alura.forumapi.domain.model.User
import org.springframework.stereotype.Service

@Service
class TopicService(
    private val courseService: CourseService,
    private val userService: UserService,
) {
    private var topics: MutableList<Topic> = mutableListOf(
        Topic(
            id = 1,
            title = "Dúvida com declaração de variável em Kotlin",
            message = "Como declaro variáveis de tipo Enum no Kotlin?",
            course = courseService.findById(1),
            user = userService.findById(1),
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

    fun findAll(): List<TopicGetDto> = topics.map { TopicGetDto.fromTopic(topic = it) }

    fun findById(id: Long): TopicGetDto? {
        val topic = topics.find { it.id == id } ?: return null

        return TopicGetDto.fromTopic(topic)
    }

    fun findAnswers(id: Long): List<Answer> = topics.find { it.id == id }?.answers ?: emptyList()

    fun create(dto: TopicPostDto): Long {
        val course = courseService.findById(dto.courseId)
        val user = userService.findById(dto.userId)

        val id = topics.size.toLong() + 1
        val topic = Topic(
            id = id,
            dto.title,
            dto.message,
            course = course,
            user = user,
        )

        topics = topics.plus(topic).toMutableList()

        return id
    }

    fun update(dto: TopicPutDto): Long {
        val topic = topics.find { it.id == dto.id } ?: return -1

        topics.indexOf(topic).let { index ->
            if (index == -1 || index >= topics.size) return -1
            topics[index] = topic.copyWith(
                dto.title,
                dto.message,
            )
        }

        return dto.id
    }

    fun remove(id: Long): Long {
        val topic = topics.find { it.id == id } ?: return -1
        topics.remove(topic)

        return id
    }
}
