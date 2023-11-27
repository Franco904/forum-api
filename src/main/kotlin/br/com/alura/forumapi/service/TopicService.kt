package br.com.alura.forumapi.service

import br.com.alura.forumapi.domain.dto.topic.GetTopicDto
import br.com.alura.forumapi.domain.dto.topic.PostTopicDto
import br.com.alura.forumapi.domain.dto.topic.PutTopicDto
import br.com.alura.forumapi.domain.model.Topic
import br.com.alura.forumapi.domain.repository.CourseRepository
import br.com.alura.forumapi.domain.repository.TopicRepository
import br.com.alura.forumapi.domain.repository.UserRepository
import br.com.alura.forumapi.exception.classes.NotFoundException
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class TopicService(
    private val topicRepository: TopicRepository,
    private val courseRepository: CourseRepository,
    private val userRepository: UserRepository,
) {
    fun findAll(
        courseName: String?,
        paging: Pageable,
    ): Page<GetTopicDto> {
        val topics = courseName?.let {
            topicRepository.findByCourseName(name = it, paging)
        } ?: topicRepository.findAll(paging)

        return topics.map { GetTopicDto.fromTopic(topic = it) }
    }

    fun findById(id: Long): GetTopicDto {
        val topic = topicRepository.findById(id).orElseThrow { NotFoundException("Topic not found!") }

        return GetTopicDto.fromTopic(topic)
    }

    @Transactional
    fun create(dto: PostTopicDto): GetTopicDto {
        val course = courseRepository.findById(dto.courseId).orElseThrow { NotFoundException("Course not found!") }
        val user = userRepository.findById(dto.userId).orElseThrow { NotFoundException("User not found!") }

        val topic = Topic(
            title = dto.title,
            message = dto.message,
            course = course,
            user = user,
        )

        topicRepository.save(topic)
        return GetTopicDto.fromTopic(topic)
    }

    @Transactional
    fun update(dto: PutTopicDto): GetTopicDto {
        val topic = topicRepository.findById(dto.id).orElseThrow { NotFoundException("Topic not found!") }
        val topicUpdated = topic.copyWith(dto.title, dto.message)

        topicRepository.save(topicUpdated)

        return GetTopicDto.fromTopic(topicUpdated)
    }

    @Transactional
    fun remove(id: Long): Long {
        val topic = topicRepository.findById(id).orElseThrow { NotFoundException("Topic not found!") }
        topicRepository.delete(topic)

        return id
    }
}
