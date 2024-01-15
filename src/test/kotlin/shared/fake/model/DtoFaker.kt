package shared.fake.model

import br.com.alura.forumapi.domain.dto.topic.PostTopicDto
import br.com.alura.forumapi.domain.dto.topic.PutTopicDto
import shared.fake.faker

fun createPostTopicDto(
    title: String? = null,
    message: String? = null,
    courseId: Long? = null,
    userId: Long? = null,
) = PostTopicDto(
    title = title ?: faker.random.randomString(5, 100),
    message = message ?: faker.random.randomString(500),
    courseId = courseId ?: faker.random.nextLong(),
    userId = userId ?: faker.random.nextLong(),
)

fun createPutTopicDto(
    id: Long? = null,
    title: String? = null,
    message: String? = null,
) = PutTopicDto(
    id = id ?: faker.random.nextLong(),
    title = title ?: faker.random.randomString(5, 100),
    message = message ?: faker.random.randomString(500),
)
