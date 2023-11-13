package br.com.alura.forumapi.domain.repository

import br.com.alura.forumapi.domain.model.Course
import org.springframework.data.jpa.repository.JpaRepository

interface CourseRepository : JpaRepository<Course, Long>
