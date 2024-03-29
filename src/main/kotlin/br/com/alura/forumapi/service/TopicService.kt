package br.com.alura.forumapi.service

import br.com.alura.forumapi.domain.dto.topic.*
import br.com.alura.forumapi.domain.model.Topic
import br.com.alura.forumapi.domain.repository.CourseRepository
import br.com.alura.forumapi.domain.repository.TopicRepository
import br.com.alura.forumapi.domain.repository.UserRepository
import br.com.alura.forumapi.exception.classes.NotFoundException
import br.com.alura.forumapi.util.Clock
import jakarta.transaction.Transactional
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class TopicService(
    private val topicRepository: TopicRepository,
    private val courseRepository: CourseRepository,
    private val userRepository: UserRepository,
) {
    @Cacheable("topics")
    fun findAll(
        courseName: String? = null,
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
    @CacheEvict("topics", allEntries = true)
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
    @CacheEvict("topics", allEntries = true)
    fun update(dto: PutTopicDto): GetTopicDto {
        val topic = topicRepository.findById(dto.id).orElseThrow { NotFoundException("Topic not found!") }

        val topicUpdated = topic.copyWith(
            dto.title,
            dto.message,
            updateDate = Clock.now(),
        )

        topicRepository.save(topicUpdated)
        return GetTopicDto.fromTopic(topicUpdated)
    }

    @Transactional
    @CacheEvict("topics", allEntries = true)
    fun remove(id: Long): Long {
        val topic = topicRepository.findById(id).orElseThrow { NotFoundException("Topic not found!") }
        topicRepository.delete(topic)

        return id
    }

    fun reportByCategory(): List<GetTopicsByCategoryDto> {
        val topicsGroupedByCategory = topicRepository.findAll().groupBy { it.course.category }

        return topicsGroupedByCategory.map { result ->
            val (category, topics) = result
            val topicDtos = topics.map { GetTopicDto.fromTopic(it) }

            GetTopicsByCategoryDto(category, topicDtos)
        }
    }

    fun reportCountByCategory(): List<GetTopicCountByCategoryDto> = topicRepository.findCountByCourseCategory()
}
