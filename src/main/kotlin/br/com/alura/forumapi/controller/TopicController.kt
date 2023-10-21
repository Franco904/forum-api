package br.com.alura.forumapi.controller

import br.com.alura.forumapi.domain.model.Topic
import br.com.alura.forumapi.service.TopicService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/topics")
class TopicController(
    private val topicService: TopicService,
) {
    @GetMapping
    fun getAll(): List<Topic> = topicService.findAll()

    @GetMapping("/{id}")
    fun getSingle(@PathVariable id: Long): Topic? = topicService.findById(id)
}
