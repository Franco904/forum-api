package br.com.alura.forumapi.domain.repository

import br.com.alura.forumapi.domain.model.Topic
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface TopicRepository : JpaRepository<Topic, Long> {
    fun findByCourseName(name: String, paging: Pageable): Page<Topic>
}
