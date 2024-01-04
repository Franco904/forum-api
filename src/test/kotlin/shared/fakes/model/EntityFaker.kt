package shared.fakes.model

import br.com.alura.forumapi.domain.model.*
import shared.fakes.faker
import java.time.LocalDateTime

object EntityFaker {
    fun createTopic(
        id: Long? = null,
        title: String? = null,
        message: String? = null,
        creationDate: LocalDateTime? = null,
        updateDate: LocalDateTime? = null,
        course: Course? = null,
        user: User? = null,
        status: StatusTopic? = null,
        answers: List<Answer>? = null,
    ) = Topic(
        id = id ?: faker.random.nextLong(),
        title = title ?: faker.random.randomString(30),
        message = message ?: faker.random.randomString(30),
        creationDate = creationDate ?: LocalDateTime.now(),
        updateDate = updateDate ?: creationDate ?: LocalDateTime.now(),
        course = course ?: createCourse(),
        user = user ?: createUser(),
        status = status ?: StatusTopic.NOT_ANSWERED,
        answers = answers ?: mutableListOf(),
    )

    fun createCourse(
        id: Long? = null,
        name: String? = null,
        category: String? = null,
    ) = Course(
        id = id ?: faker.random.nextLong(),
        name = name ?: faker.random.randomString(30),
        category = category ?: faker.random.randomString(30),
    )

    fun createUser(
        id: Long? = null,
        name: String? = null,
        email: String? = null,
        password: String? = null,
        roles: List<Role>? = null,
    ) = User(
        id = id ?: faker.random.nextLong(),
        name = name ?: faker.random.randomString(30),
        email = email ?: faker.random.randomString(30),
        password = password ?: faker.random.randomString(30),
        roles = roles ?: mutableListOf(createRole()),
    )

    private fun createRole(
        id: Long? = null,
        name: String? = null,
    ) = Role(
        id = id ?: faker.random.nextLong(),
        name = name ?: faker.random.randomString(30),
    )

    fun createAnswer(
        id: Long? = null,
        message: String? = null,
        creationDate: LocalDateTime? = null,
        user: User? = null,
        topic: Topic? = null,
        hasSolvedTopic: Boolean? = null,
    ) = Answer(
        id = id ?: faker.random.nextLong(),
        message = message ?: faker.random.randomString(30),
        creationDate = creationDate ?: LocalDateTime.now(),
        user = user ?: createUser(),
        topic = topic ?: createTopic(),
        hasSolvedTopic = hasSolvedTopic ?: faker.random.nextBoolean(),
    )
}
