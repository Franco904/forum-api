package br.com.alura.forumapi.controller

import br.com.alura.forumapi.domain.dto.topic.*
import br.com.alura.forumapi.service.TopicService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/topics")
class TopicController(
    private val topicService: TopicService,
) {
    @GetMapping
    fun getAll(): List<GetTopicDto> = topicService.findAll()

    @GetMapping("/{id}")
    fun getSingle(@PathVariable id: Long): GetTopicDto? = topicService.findById(id)

    @PostMapping
    fun post(@RequestBody @Valid topic: PostTopicDto): Long = topicService.create(topic)

    @PutMapping
    fun put(@RequestBody @Valid topic: PutTopicDto): Long = topicService.update(topic)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): Long = topicService.remove(id)
}
