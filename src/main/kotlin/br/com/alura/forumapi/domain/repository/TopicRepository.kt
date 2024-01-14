package br.com.alura.forumapi.domain.repository

import br.com.alura.forumapi.domain.dto.topic.GetTopicCountByCategoryDto
import br.com.alura.forumapi.domain.model.Course
import br.com.alura.forumapi.domain.model.Topic
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TopicRepository : JpaRepository<Topic> {
    fun findByCourseName(name: String, paging: Pageable): Page<Topic>

    @Query("""
        SELECT new ${GetTopicCountByCategoryDto.PATH}(c.category, COUNT(t) AS count)
        FROM ${Topic.TABLE_NAME} AS t
        INNER JOIN ${Course.TABLE_NAME} AS c
        ON t.course.id = c.id
        GROUP BY c.category
    """)
    fun findCountByCourseCategory(): List<GetTopicCountByCategoryDto>
}
