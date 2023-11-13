package br.com.alura.forumapi.service

import br.com.alura.forumapi.domain.model.Answer
import br.com.alura.forumapi.domain.repository.AnswerRepository
import org.springframework.stereotype.Service

@Service
class AnswerService(
    private val answerRepository: AnswerRepository,
) {
    fun findAll(): List<Answer> = answerRepository.findAll()
}
