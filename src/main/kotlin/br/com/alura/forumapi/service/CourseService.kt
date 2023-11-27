package br.com.alura.forumapi.service

import br.com.alura.forumapi.domain.dto.course.GetCourseDto
import br.com.alura.forumapi.domain.dto.course.PostCourseDto
import br.com.alura.forumapi.domain.model.Course
import br.com.alura.forumapi.domain.repository.CourseRepository
import br.com.alura.forumapi.exception.classes.NotFoundException
import jakarta.transaction.Transactional
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class CourseService(
    private val courseRepository: CourseRepository,
) {
    @Cacheable("courses")
    fun findAll(): List<GetCourseDto> {
        val courses = courseRepository.findAll()

        return courses.map { GetCourseDto.fromCourse(course = it) }
    }

    fun findById(id: Long): GetCourseDto {
        val course = courseRepository.findById(id).orElseThrow { NotFoundException("Course not found!") }

        return GetCourseDto.fromCourse(course)
    }

    @Transactional
    @CacheEvict("courses", allEntries = true)
    fun create(dto: PostCourseDto): GetCourseDto {
        val course = Course(
            name = dto.name,
            category = dto.category,
        )

        courseRepository.save(course)
        return GetCourseDto.fromCourse(course)
    }

    @Transactional
    @CacheEvict("courses", allEntries = true)
    fun remove(id: Long): Long {
        val course = courseRepository.findById(id).orElseThrow { NotFoundException("Topic not found!") }
        courseRepository.delete(course)

        return id
    }
}
