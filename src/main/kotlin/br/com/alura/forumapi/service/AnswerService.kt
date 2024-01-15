package br.com.alura.forumapi.service

import br.com.alura.forumapi.domain.dto.answer.GetAnswerDto
import br.com.alura.forumapi.domain.dto.answer.PostAnswerDto
import br.com.alura.forumapi.domain.model.Answer
import br.com.alura.forumapi.domain.repository.AnswerRepository
import br.com.alura.forumapi.domain.repository.TopicRepository
import br.com.alura.forumapi.domain.repository.UserRepository
import br.com.alura.forumapi.exception.classes.NotFoundException
import br.com.alura.forumapi.service.mail.EmailService
import jakarta.transaction.Transactional
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class AnswerService(
    private val answerRepository: AnswerRepository,
    private val topicRepository: TopicRepository,
    private val userRepository: UserRepository,
    private val emailService: EmailService,
) {
    @Cacheable("answers")
    fun findAllByTopicId(topicId: Long): List<GetAnswerDto> {
        return answerRepository.findByTopicId(topicId).map { GetAnswerDto.fromAnswer(answer = it) }
    }

    @Transactional
    @CacheEvict("answers", allEntries = true)
    fun create(dto: PostAnswerDto, topicId: Long): GetAnswerDto {
        val topic = topicRepository.findById(topicId).orElseThrow { NotFoundException("Topic not found!") }
        val user = userRepository.findById(dto.userId).orElseThrow { NotFoundException("User not found!") }

        val answer = Answer(
            message = dto.message,
            topic = topic,
            user = user,
        )

        answerRepository.save(answer)
        emailService.notifyRecipient(targetEmail = answer.topic.user.email)

        return GetAnswerDto.fromAnswer(answer)
    }
}
