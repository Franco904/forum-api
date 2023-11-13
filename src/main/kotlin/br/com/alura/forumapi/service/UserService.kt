package br.com.alura.forumapi.service

import br.com.alura.forumapi.domain.model.User
import br.com.alura.forumapi.domain.repository.UserRepository
import br.com.alura.forumapi.exception.classes.NotFoundException
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    fun findById(id: Long): User = userRepository.findById(id).orElseThrow { NotFoundException("User not found!") }
}
