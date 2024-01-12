package br.com.alura.forumapi.controller

import br.com.alura.forumapi.domain.dto.topic.GetTopicCountByCategoryDto
import br.com.alura.forumapi.domain.dto.topic.GetTopicsByCategoryDto
import br.com.alura.forumapi.service.TopicService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/report")
class ReportController(
    private val topicService: TopicService,
) {
    @GetMapping("/category")
    fun reportByCategory(): List<GetTopicsByCategoryDto> = topicService.reportByCategory()

    @GetMapping("/category-count")
    fun reportCountByCategory(): List<GetTopicCountByCategoryDto> = topicService.reportCountByCategory()
}
