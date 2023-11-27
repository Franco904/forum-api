package br.com.alura.forumapi.controller

import br.com.alura.forumapi.domain.dto.course.GetCourseDto
import br.com.alura.forumapi.domain.dto.course.PostCourseDto
import br.com.alura.forumapi.service.CourseService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriBuilder
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/courses")
class CourseController(
    private val courseService: CourseService,
) {
    @GetMapping
    fun getAll(): List<GetCourseDto> = courseService.findAll()

    @GetMapping("/{id}")
    fun getSingle(@PathVariable id: Long): GetCourseDto = courseService.findById(id)

    @PostMapping
    fun post(
        @RequestBody @Valid courseDto: PostCourseDto,
        uriBuilder: UriComponentsBuilder,
    ): ResponseEntity<GetCourseDto> {
        val course = courseService.create(courseDto)
        val uri = uriBuilder.path("/courses/${course.id}").build().toUri()

        return ResponseEntity.created(uri).body(course)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): Long = courseService.remove(id)
}
