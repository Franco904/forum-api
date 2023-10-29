package br.com.alura.forumapi.controller

import br.com.alura.forumapi.domain.dto.topic.TopicInputDto
import br.com.alura.forumapi.domain.dto.topic.TopicOutputDto
import br.com.alura.forumapi.service.TopicService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/topics")
class TopicController(
    private val topicService: TopicService,
) {
    @GetMapping
    fun getAll(): List<TopicOutputDto> = topicService.findAll()

    @GetMapping("/{id}")
    fun getSingle(@PathVariable id: Long): TopicOutputDto? = topicService.findById(id)

    @PostMapping
    fun post(@RequestBody @Valid topic: TopicInputDto) = topicService.create(topic)
}
