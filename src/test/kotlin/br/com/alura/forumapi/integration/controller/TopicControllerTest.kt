package br.com.alura.forumapi.integration.controller

import br.com.alura.forumapi.domain.model.Role
import br.com.alura.forumapi.domain.repository.UserRepository
import br.com.alura.forumapi.security.util.JwtUtil
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TopicControllerTest {
    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    @Autowired
    private lateinit var userRepository: UserRepository

    private var token: String? = null

    companion object {
        private const val RESOURCE_ENDPOINT = "/topics"

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

    private fun generateToken(isAuthorized: Boolean): String? {
        val authorities = mutableListOf(Role(2, "LEITURA_ESCRITA"))

        val authorizedUser = userRepository.findById(1).get()
        val notAuthorizedUser = userRepository.findById(2).get()

        val user = if (isAuthorized) authorizedUser else notAuthorizedUser

        return jwtUtil.generateToken(user.email, authorities)
    }

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply<DefaultMockMvcBuilder>(
            SecurityMockMvcConfigurers.springSecurity(),
        ).build()
    }

    @Nested
    @DisplayName("Get all topics")
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
        fun `Deve retornar um codigo de sucesso na resposta caso o usuario esteja autenticado e autorizado a acessar o endpoint`() {
            token = generateToken(isAuthorized = true)

            mockMvc.get(RESOURCE_ENDPOINT) {
                headers {
                    contentType = MediaType.APPLICATION_JSON
                    token?.let { token -> setBearerAuth(token) }
                }
            }.andExpect { status { isOk() } }
        }

        @Test
        fun `Deve retornar todos os topicos cadastrados no banco de dados`() {
        }

        @Test
        fun `Deve retornar todos os topicos cadastrados no banco de dados com o nome de curso informado`() {
        }
    }
}
