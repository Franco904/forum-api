package br.com.alura.forumapi.domain.dto.course

import br.com.alura.forumapi.domain.model.Course

data class GetCourseDto(
    val id: Long? = null,
    val name: String,
    val category: String,
) {
    companion object {
        fun fromCourse(course: Course): GetCourseDto {
            return GetCourseDto(
                id = course.id,
                name = course.name,
                category = course.category,
            )
        }
    }
}
