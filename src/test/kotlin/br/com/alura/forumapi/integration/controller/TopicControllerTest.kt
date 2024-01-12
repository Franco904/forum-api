package br.com.alura.forumapi.integration.controller

import br.com.alura.forumapi.domain.dto.topic.GetTopicDto
import br.com.alura.forumapi.domain.model.Course
import br.com.alura.forumapi.domain.model.Role
import br.com.alura.forumapi.domain.model.User
import br.com.alura.forumapi.domain.repository.TopicRepository
import br.com.alura.forumapi.security.util.JwtUtil
import br.com.alura.forumapi.util.Clock
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import shared.fake.model.UserRole
import shared.fake.model.createTopic
import shared.util.jpa.clearAllTables
import shared.util.jpa.populateDomainTables
import shared.util.rest.PageResponse
import shared.util.rest.contentSubstring
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TopicControllerTest {
    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    private var token: String? = null

    @Autowired
    private lateinit var topicRepository: TopicRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    private val courses = listOf(
        Course(id = 1, name = "Kotlin: Programação funcional", category = "Kotlin"),
        Course(id = 2, name = "Layouts no Android", category = "Android"),
        Course(id = 3, name = "Integração com API web em Flutter", category = "Flutter"),
    )
    private val users = listOf(
        User(id = 1, name = "authorizedUser", email = "authorizedUser@user", password = "123456"),
        User(id = 2, name = "notAuthorizedUser", email = "notAuthorizedUser@user", password = "123456"),
    )
    private val roles = mutableListOf(Role(id = 1, name = "READ_WRITE"))
    private val userRoles = listOf(UserRole(id = 1, userId = 1, roleId = 1))

    companion object {
        private const val RESOURCE_ENDPOINT = "/topics"

        @Container
        private val mysqlContainer = MySQLContainer<Nothing>("mysql:latest").apply {
            withDatabaseName("forum-test")
            withUsername("test")
            withPassword("123123as")
        }

        @Container
        private val redisContainer = GenericContainer<Nothing>("redis:latest").apply {
            withExposedPorts(6379)
        }

        @JvmStatic
        @DynamicPropertySource
        fun getProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl)
            registry.add("spring.datasource.username", mysqlContainer::getUsername)
            registry.add("spring.datasource.password", mysqlContainer::getPassword)

            registry.add("spring.data.redis.host", redisContainer::getHost)
            registry.add("spring.data.redis.port", redisContainer::getFirstMappedPort)
        }
    }

    private fun generateToken(isAuthorized: Boolean): String? {
        val user = if (isAuthorized) users[0] else users[1]

        return jwtUtil.generateToken(user.email, roles)
    }

    @BeforeEach
    fun setUp() {
        Clock.setNowForTesting(LocalDateTime.now())

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply<DefaultMockMvcBuilder>(
            SecurityMockMvcConfigurers.springSecurity(),
        ).build()

        jdbcTemplate.clearAllTables()
        jdbcTemplate.populateDomainTables(courses, users, roles, userRoles)

        mysqlContainer.isRunning.shouldBeTrue()
    }

    @Nested
    @DisplayName("Get all")
    inner class GetAllTopicsTests {
        @Test
        fun `Deve retornar um codigo de erro forbidden na resposta caso o usuario nao esteja autenticado`() {
            mockMvc.get(RESOURCE_ENDPOINT) {
                headers {
                    contentType = MediaType.APPLICATION_JSON
                }
            }.andExpect { status { isForbidden() } }
        }

        @Test
        fun `Deve retornar um codigo de erro forbidden na resposta caso o usuario esteja autenticado mas nao esteja autorizado a acessar o endpoint`() {
            token = generateToken(isAuthorized = false)

            mockMvc.get(RESOURCE_ENDPOINT) {
                headers {
                    contentType = MediaType.APPLICATION_JSON
                    token?.let { token -> setBearerAuth(token) }
                }
            }.andExpect { status { isForbidden() } }
        }

        @Test
        fun `Deve retornar todos os topicos cadastrados no banco de dados`() {
            val topic1 = createTopic(course = courses[0], user = users.first())
            val topic2 = createTopic(course = courses[1], user = users.first())

            topicRepository.save(topic1)
            topicRepository.save(topic2)

            token = generateToken(isAuthorized = true)

            val result = mockMvc.get(RESOURCE_ENDPOINT) {
                headers {
                    contentType = MediaType.APPLICATION_JSON
                    token?.let { token -> setBearerAuth(token) }
                }
            }.andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
            }.andReturn()

            val responseContent = result.response.contentAsString.contentSubstring()
            val expectedResponseContent = PageResponse(
                content = listOf(
                    GetTopicDto.fromTopic(topic1),
                    GetTopicDto.fromTopic(topic2),
                )
            ).toJson().contentSubstring()

            responseContent.shouldBeEqualTo(expectedResponseContent)
        }

        @Test
        fun `Deve retornar todos os topicos cadastrados no banco de dados com o nome de curso informado`() {
            Clock.setNowForTesting(LocalDateTime.now())
            val topic1 = createTopic(course = courses[0], user = users.first())
            val topic2 = createTopic(course = courses[1], user = users.first())

            topicRepository.save(topic1)
            topicRepository.save(topic2)

            token = generateToken(isAuthorized = true)

            val result = mockMvc.get(RESOURCE_ENDPOINT) {
                headers {
                    contentType = MediaType.APPLICATION_JSON
                    token?.let { token -> setBearerAuth(token) }
                }
                param("courseName", courses[0].name)
            }.andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
            }.andReturn()

            val responseContent = result.response.contentAsString.contentSubstring()
            val expectedResponseContent = PageResponse(
                content = listOf(
                    GetTopicDto.fromTopic(topic1),
                )
            ).toJson().contentSubstring()

            responseContent.shouldBeEqualTo(expectedResponseContent)
        }
    }
}
