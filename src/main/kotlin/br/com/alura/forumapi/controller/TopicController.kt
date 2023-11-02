package br.com.alura.forumapi.controller

import br.com.alura.forumapi.domain.dto.topic.GetTopicDto
import br.com.alura.forumapi.domain.dto.topic.PostTopicDto
import br.com.alura.forumapi.domain.dto.topic.PutTopicDto
import br.com.alura.forumapi.service.TopicService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

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
    fun post(
        @RequestBody @Valid topicDto: PostTopicDto,
        uriBuilder: UriComponentsBuilder,
    ): ResponseEntity<GetTopicDto> {
        val topic = topicService.create(topicDto)
        val uri = uriBuilder.path("/topics/${topic.id}").build().toUri()

        // Http Status Code inferred: 201
        return ResponseEntity.created(uri).body(topic)
    }

    @PutMapping
    fun put(@RequestBody @Valid topicDto: PutTopicDto): ResponseEntity<GetTopicDto> {
        val topic = topicService.update(topicDto)

        return ResponseEntity.ok(topic)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): Long = topicService.remove(id)
}
