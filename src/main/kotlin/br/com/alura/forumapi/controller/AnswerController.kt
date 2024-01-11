package br.com.alura.forumapi.controller

import br.com.alura.forumapi.domain.dto.answer.GetAnswerDto
import br.com.alura.forumapi.domain.dto.answer.PostAnswerDto
import br.com.alura.forumapi.service.AnswerService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/topics/{topicId}/answers")
@SecurityRequirement(name = "Bearer Auth")
class AnswerController(
    private val answerService: AnswerService,
) {
    @GetMapping
    fun getAllByTopic(@PathVariable topicId: Long): List<GetAnswerDto> = answerService.findAllByTopicId(topicId)

    @PostMapping
    fun post(
        @RequestBody @Valid answerDto: PostAnswerDto,
        @PathVariable topicId: Long,
        uriBuilder: UriComponentsBuilder,
    ): ResponseEntity<GetAnswerDto> {
        val answer = answerService.create(answerDto, topicId)
        val uri = uriBuilder.path("/answers/${answer.id}").build().toUri()

        // Http Status Code inferred: 201
        return ResponseEntity.created(uri).body(answer)
    }
}
