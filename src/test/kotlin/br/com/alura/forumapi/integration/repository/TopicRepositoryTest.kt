package br.com.alura.forumapi.integration.repository

import br.com.alura.forumapi.domain.dto.topic.GetTopicCountByCategoryDto
import br.com.alura.forumapi.domain.model.Course
import br.com.alura.forumapi.domain.model.Role
import br.com.alura.forumapi.domain.model.User
import br.com.alura.forumapi.domain.repository.TopicRepository
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import shared.fake.model.EntityFaker
import shared.fake.model.UserRole
import shared.util.jpa.clearAllTables
import shared.util.jpa.populateDomainTables

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TopicRepositoryTest {
    @Autowired
    private lateinit var sut: TopicRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    private val courses = listOf(
        Course(id = 1, name = "Kotlin: Programação funcional", category = "Kotlin"),
        Course(id = 2, name = "Layouts no Android", category = "Android"),
        Course(id = 3, name = "Integração com API web em Flutter", category = "Flutter"),
    )
    private val users = listOf(User(id = 1, name = "test123", email = "test123@user", password = "password"))
    private val roles = listOf(Role(id = 1, name = "READ_WRITE"))
    private val userRoles = listOf(UserRole(id = 1, userId = 1, roleId = 1))

    companion object {
        @Container
        private val mysqlContainer = MySQLContainer<Nothing>("mysql:latest").apply {
            withDatabaseName("forum-test")
            withUsername("test")
            withPassword("123123as")
        }

        @JvmStatic
        @DynamicPropertySource
        fun getProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl)
            registry.add("spring.datasource.username", mysqlContainer::getUsername)
            registry.add("spring.datasource.password", mysqlContainer::getPassword)
        }
    }

    @BeforeEach
    fun setUp() {
        jdbcTemplate.clearAllTables()
        jdbcTemplate.populateDomainTables(courses, users, roles, userRoles)

        mysqlContainer.isRunning.shouldBeTrue()
    }

    @Nested
    @DisplayName("findByCourseName")
    inner class FindByCourseNameTest {
        @Test
        fun `Deve retornar uma lista de topicos filtrados pelo nome do curso correto`() {
            val topic1 = EntityFaker.createTopic(course = courses[0], user = users.first())
            val topic2 = EntityFaker.createTopic(course = courses[1], user = users.first())
            val topic3 = EntityFaker.createTopic(course = courses[2], user = users.first())
            val topic4 = EntityFaker.createTopic(course = courses[0], user = users.first())

            sut.save(topic1)
            sut.save(topic2)
            sut.save(topic3)
            sut.save(topic4)

            val topics = sut.findByCourseName(
                "Integração com API web em Flutter",
                PageRequest.of(0, 10),
            )

            topics.content.size.shouldBeEqualTo(1)
            topics.content.first().course.id.shouldBeEqualTo(topic3.course.id)
            topics.content.first().course.name.shouldBeEqualTo(topic3.course.name)
        }
    }

    @Nested
    @DisplayName("findCountByCourseCategory")
    inner class FindCountByCourseCategoryTest {
        @Test
        fun `Deve retornar uma lista com a contagem correta de topicos registrados por categoria`() {
            val topic1 = EntityFaker.createTopic(course = courses[0], user = users.first())
            val topic2 = EntityFaker.createTopic(course = courses[1], user = users.first())
            val topic3 = EntityFaker.createTopic(course = courses[2], user = users.first())
            val topic4 = EntityFaker.createTopic(course = courses[0], user = users.first())

            sut.save(topic1)
            sut.save(topic2)
            sut.save(topic3)
            sut.save(topic4)

            val topics = sut.findCountByCourseCategory()

            topics.shouldBeEqualTo(listOf(
                GetTopicCountByCategoryDto(category = "Kotlin", topicCount = 2),
                GetTopicCountByCategoryDto(category = "Android", topicCount = 1),
                GetTopicCountByCategoryDto(category = "Flutter", topicCount = 1),
            ))
        }
    }
}
