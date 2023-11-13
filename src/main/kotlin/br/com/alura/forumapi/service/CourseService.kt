package br.com.alura.forumapi.service

import br.com.alura.forumapi.domain.model.Course
import br.com.alura.forumapi.domain.repository.CourseRepository
import br.com.alura.forumapi.exception.classes.NotFoundException
import org.springframework.stereotype.Service

@Service
class CourseService(
    private val courseRepository: CourseRepository,
) {
    fun findById(id: Long): Course = courseRepository.findById(id).orElseThrow { NotFoundException("Course not found!") }
}
