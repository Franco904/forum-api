package br.com.alura.forumapi.service

import br.com.alura.forumapi.domain.dto.topic.GetTopicDto
import br.com.alura.forumapi.domain.repository.CourseRepository
import br.com.alura.forumapi.domain.repository.TopicRepository
import br.com.alura.forumapi.domain.repository.UserRepository
import br.com.alura.forumapi.exception.classes.NotFoundException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import test_utils.faker.faker
import test_utils.faker.model.EntityFaker
import java.util.*

class TopicServiceTest {
    val topicRepository: TopicRepository = mockk()
    val courseRepository: CourseRepository = mockk()
    val userRepository: UserRepository = mockk()

    val pageable: Pageable = mockk()

    val sut = TopicService(
        topicRepository,
        courseRepository,
        userRepository,
    )

    @Nested
    @DisplayName("findAll")
    inner class FindAllTest {
        @Test
        fun `Deve retornar uma lista de topicos com os dados corretos dos topicos recuperados pelo nome curso se informado`() {
            val courseName = "Kotlin"
            val topic1 = EntityFaker.createTopic()
            val topic2 = EntityFaker.createTopic()
            val topic3 = EntityFaker.createTopic()
            val topicList = listOf(topic1, topic2, topic3)

            every { topicRepository.findByCourseName(courseName, pageable) }.returns(PageImpl(topicList))

            val topics = sut.findAll(courseName, pageable)

            topics.content.shouldBeEqualTo(topicList.map { GetTopicDto.fromTopic(topic = it) })
            verify(exactly = 0) { topicRepository.findAll(any<Pageable>()) }
        }

        @Test
        fun `Deve retornar uma lista de topicos com os tópicos recuperados sem filtros se o nome do curso nao for informado`() {
            val topic1 = EntityFaker.createTopic()
            val topic2 = EntityFaker.createTopic()
            val topic3 = EntityFaker.createTopic()
            val topicList = listOf(topic1, topic2, topic3)

            every { topicRepository.findAll(pageable) }.returns(PageImpl(topicList))

            val topics = sut.findAll(paging = pageable)

            topics.content.shouldBeEqualTo(topicList.map { GetTopicDto.fromTopic(topic = it) })
            verify(exactly = 0) { topicRepository.findByCourseName(any<String>(), any<Pageable>()) }
        }
    }

    @Nested
    @DisplayName("findById")
    inner class FindByIdTest {
        @Test
        fun `Deve retornar um topico a partir do id informado se ele estiver registrado no banco de dados`() {
            val id = faker.random.nextLong()
            val topic = EntityFaker.createTopic(id = id)

            every { topicRepository.findById(id) }.returns(Optional.of(topic))

            val resultTopic = sut.findById(id)
            resultTopic.shouldBeEqualTo(GetTopicDto.fromTopic(topic))
        }

        @Test
        fun `Deve lancar uma excecao se nao existir um topico registrado para o id informado`() {
            val id = faker.random.nextLong()

            every { topicRepository.findById(id) }.returns(Optional.empty())

            invoking { sut.findById(id) }.shouldThrow(NotFoundException("Topic not found!"))
        }
    }

    @Nested
    @DisplayName("create")
    inner class CreateTest {
        @Test
        fun `Deve criar um topico no banco de dados com os dados corretos e retornar os dados corretos`() {
        }

        @Test
        fun `Deve lancar uma excecao se nao existir registrado um curso registrado para o id informado`() {
        }

        @Test
        fun `Deve lancar uma excecao se nao existir registrado um usuario registrado para o id informado`() {
        }
    }

    @Nested
    @DisplayName("update")
    inner class UpdateTest {
        @Test
        fun `Deve atualizar um topico no banco de dados com os dados corretos e retornar os dados corretos`() {
        }

        @Test
        fun `Deve lancar uma excecao se nao existir um topico registrado para o id informado`() {
        }
    }

    @Nested
    @DisplayName("remove")
    inner class RemoveTest {
        @Test
        fun `Deve remover um topico no banco de dados e retornar o id do topico`() {
        }

        @Test
        fun `Deve lancar uma excecao se nao existir um topico registrado para o id informado`() {
        }
    }

    @Nested
    @DisplayName("reportByCategory")
    inner class ReportByCategoryTest {
        @Test
        fun `Deve retornar uma lista de topicos agrupados por categoria`() {
        }
    }

    @Nested
    @DisplayName("reportCountByCategory")
    inner class ReportCountByCategoryTest {
        @Test
        fun `Deve retornar uma lista da contagem de topicos por categoria`() {
        }
    }
}
