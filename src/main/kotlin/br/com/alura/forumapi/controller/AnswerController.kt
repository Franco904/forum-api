package br.com.alura.forumapi.controller

import br.com.alura.forumapi.domain.model.Answer
import br.com.alura.forumapi.service.AnswerService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/topics/{topicId}/answers")
class AnswerController(
    private val answerService: AnswerService,
) {
    @GetMapping
    fun getAllByTopic(@PathVariable topicId: Long): List<Answer> = answerService.findAll()
}
