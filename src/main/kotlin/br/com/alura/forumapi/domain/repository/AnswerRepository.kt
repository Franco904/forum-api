package br.com.alura.forumapi.domain.repository

import br.com.alura.forumapi.domain.model.Answer
import org.springframework.data.jpa.repository.JpaRepository

interface AnswerRepository : JpaRepository<Answer, Long>
