package br.com.alura.forumapi.domain.repository

import br.com.alura.forumapi.domain.model.Topic
import org.springframework.data.jpa.repository.JpaRepository

interface TopicRepository : JpaRepository<Topic, Long>
