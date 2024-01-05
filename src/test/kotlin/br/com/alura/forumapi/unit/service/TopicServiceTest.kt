package br.com.alura.forumapi.unit.service

import br.com.alura.forumapi.domain.dto.topic.GetTopicCountByCategoryDto
import br.com.alura.forumapi.domain.dto.topic.GetTopicDto
import br.com.alura.forumapi.domain.dto.topic.GetTopicsByCategoryDto
import br.com.alura.forumapi.domain.model.Topic
import br.com.alura.forumapi.domain.repository.CourseRepository
import br.com.alura.forumapi.domain.repository.TopicRepository
import br.com.alura.forumapi.domain.repository.UserRepository
import br.com.alura.forumapi.exception.classes.NotFoundException
import br.com.alura.forumapi.service.TopicService
import br.com.alura.forumapi.util.Clock
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import shared.fake.faker
import shared.fake.model.DtoFaker
import shared.fake.model.EntityFaker
import java.time.LocalDateTime
import java.util.*

class TopicServiceTest {
    val topicRepository: TopicRepository = mockk(relaxed = true)
    val courseRepository: CourseRepository = mockk(relaxed = true)
    val userRepository: UserRepository = mockk(relaxed = true)

    val pageable: Pageable = mockk(relaxed = true)

    val sut = TopicService(
        topicRepository,
        courseRepository,
        userRepository,
    )

    @BeforeEach
    fun setUp() {
        Clock.setNowForTesting(LocalDateTime.now())
        clearAllMocks()
    }

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
        fun `Deve retornar uma lista de topicos com os topicos recuperados sem filtros se o nome do curso nao for informado`() {
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
            val course = EntityFaker.createCourse()
            val user = EntityFaker.createUser()

            every { courseRepository.findById(course.id!!) }.returns(Optional.of(course))
            every { userRepository.findById(user.id!!) }.returns(Optional.of(user))

            val postTopicDto = DtoFaker.createPostTopicDto(
                courseId = course.id,
                userId = user.id,
            )

            val topic = Topic(
                title = postTopicDto.title,
                message = postTopicDto.message,
                course = course,
                user = user,
            )

            every { topicRepository.save(topic) }.returns(topic)

            val resultTopic = sut.create(postTopicDto)

            verify(exactly = 1) { topicRepository.save(topic) }
            resultTopic.shouldBeEqualTo(GetTopicDto.fromTopic(topic))
        }

        @Test
        fun `Deve lancar uma excecao se nao existir registrado um curso registrado para o id informado`() {
            val course = EntityFaker.createCourse()
            val user = EntityFaker.createUser()

            every { courseRepository.findById(course.id!!) }.returns(Optional.empty())
            every { userRepository.findById(user.id!!) }.returns(Optional.of(user))

            val postTopicDto = DtoFaker.createPostTopicDto(
                courseId = course.id,
                userId = user.id,
            )

            invoking { sut.create(postTopicDto) }.shouldThrow(NotFoundException("Course not found!"))
            verify(exactly = 0) { topicRepository.save(any<Topic>()) }
        }

        @Test
        fun `Deve lancar uma excecao se nao existir registrado um usuario registrado para o id informado`() {
            val course = EntityFaker.createCourse()
            val user = EntityFaker.createUser()

            every { courseRepository.findById(course.id!!) }.returns(Optional.of(course))
            every { userRepository.findById(user.id!!) }.returns(Optional.empty())

            val postTopicDto = DtoFaker.createPostTopicDto(
                courseId = course.id,
                userId = user.id,
            )

            invoking { sut.create(postTopicDto) }.shouldThrow(NotFoundException("User not found!"))
            verify(exactly = 0) { topicRepository.save(any<Topic>()) }
        }
    }

    @Nested
    @DisplayName("update")
    inner class UpdateTest {
        @Test
        fun `Deve atualizar um topico no banco de dados com os dados corretos e retornar os dados corretos`() {
            val topic = EntityFaker.createTopic()
            every { topicRepository.findById(topic.id!!) }.returns(Optional.of(topic))

            val putTopicDto = DtoFaker.createPutTopicDto(id = topic.id)
            val topicUpdated = topic.copyWith(
                title = putTopicDto.title,
                message = putTopicDto.message,
                updateDate = Clock.now(),
            )

            every { topicRepository.save(topicUpdated) }.returns(topicUpdated)

            val resultTopic = sut.update(putTopicDto)

            verify(exactly = 1) { topicRepository.save(topicUpdated) }
            resultTopic.shouldBeEqualTo(GetTopicDto.fromTopic(topicUpdated))
        }

        @Test
        fun `Deve lancar uma excecao se nao existir um topico registrado para o id informado`() {
            val topic = EntityFaker.createTopic()
            every { topicRepository.findById(topic.id!!) }.returns(Optional.empty())

            val putTopicDto = DtoFaker.createPutTopicDto(id = topic.id)

            invoking { sut.update(putTopicDto) }.shouldThrow(NotFoundException("Topic not found!"))
            verify(exactly = 0) { topicRepository.save(any<Topic>()) }
        }
    }

    @Nested
    @DisplayName("remove")
    inner class RemoveTest {
        @Test
        fun `Deve remover um topico no banco de dados e retornar o id do topico`() {
            val topic = EntityFaker.createTopic()
            every { topicRepository.findById(topic.id!!) }.returns(Optional.of(topic))

            val id = sut.remove(topic.id!!)

            verify(exactly = 1) { topicRepository.delete(topic) }
            id.shouldBeEqualTo(topic.id)
        }

        @Test
        fun `Deve lancar uma excecao se nao existir um topico registrado para o id informado`() {
            val id = faker.random.nextLong()
            every { topicRepository.findById(id) }.returns(Optional.empty())

            invoking { sut.remove(id) }.shouldThrow(NotFoundException("Topic not found!"))

            verify(exactly = 0) { topicRepository.delete(any<Topic>()) }
        }
    }

    @Nested
    @DisplayName("reportByCategory")
    inner class ReportByCategoryTest {
        @Test
        fun `Deve retornar uma lista de topicos agrupados por categoria`() {
            val topics = listOf(
                EntityFaker.createTopic(id = 1, course = EntityFaker.createCourse(category = "Kotlin")),
                EntityFaker.createTopic(id = 2, course = EntityFaker.createCourse(category = "Android")),
                EntityFaker.createTopic(id = 3, course = EntityFaker.createCourse(category = "Flutter")),
                EntityFaker.createTopic(id = 4, course = EntityFaker.createCourse(category = "Kotlin")),
            )

            val dtos = topics.map { topic -> GetTopicDto.fromTopic(topic) }

            every { topicRepository.findAll() }.returns(topics)

            val topicsGrouped = sut.reportByCategory()

            topicsGrouped.shouldBeEqualTo(listOf(
                GetTopicsByCategoryDto(category = "Kotlin", topics = listOf(dtos[0], dtos[3])),
                GetTopicsByCategoryDto(category = "Android", topics = listOf(dtos[1])),
                GetTopicsByCategoryDto(category = "Flutter", topics = listOf(dtos[2])),
            ))
        }
    }

    @Nested
    @DisplayName("reportCountByCategory")
    inner class ReportCountByCategoryTest {
        @Test
        fun `Deve retornar uma lista da contagem de topicos por categoria`() {
            val topicCountByCategory = listOf(
                GetTopicCountByCategoryDto(category = "Kotlin", topicCount = 2),
                GetTopicCountByCategoryDto(category = "Android", topicCount = 1),
                GetTopicCountByCategoryDto(category = "Flutter", topicCount = 1),
            )

            every { topicRepository.findCountByCourseCategory() }.returns(topicCountByCategory)

            val result = sut.reportCountByCategory()

            verify(exactly = 1) { topicRepository.findCountByCourseCategory() }
            result.shouldBeEqualTo(topicCountByCategory)
        }
    }
}
