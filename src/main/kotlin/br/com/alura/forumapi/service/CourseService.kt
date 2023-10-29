package br.com.alura.forumapi.service

import br.com.alura.forumapi.domain.model.Course
import org.springframework.stereotype.Service

@Service
class CourseService {
    private val courses: MutableList<Course> by lazy {
        mutableListOf(
            Course(
                id = 1,
                name = "Kotlin",
                category = "Programming Languages",
            ),
            Course(
                id = 2,
                name = "IntelliJ",
                category = "IDEs",
            ),
        )
    }

    fun findById(id: Long): Course = courses.find { it.id == id } ?: throw Exception("Course not found!")
}
