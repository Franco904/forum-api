package test_utils.faker.model

import br.com.alura.forumapi.domain.dto.topic.PostTopicDto
import test_utils.faker.faker

object DtoFaker {
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
}
