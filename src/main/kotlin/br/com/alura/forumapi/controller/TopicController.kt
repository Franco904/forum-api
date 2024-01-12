package br.com.alura.forumapi.controller

import br.com.alura.forumapi.domain.dto.topic.*
import br.com.alura.forumapi.service.TopicService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/topics")
class TopicController(
    private val topicService: TopicService,
) {
    @GetMapping
    fun getAll(
        @RequestParam(required = false) courseName: String?,
        paging: Pageable,
    ): Page<GetTopicDto> {
        return topicService.findAll(courseName, paging)
    }

    @GetMapping("/{id}")
    fun getSingle(@PathVariable id: Long): GetTopicDto = topicService.findById(id)

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

    @GetMapping("/report-by-category")
    fun reportByCategory(): List<GetTopicsByCategoryDto> = topicService.reportByCategory()

    @GetMapping("/report-by-category-count")
    fun reportCountByCategory(): List<GetTopicCountByCategoryDto> = topicService.reportCountByCategory()
}
