package br.com.alura.forumapi.service

import br.com.alura.forumapi.domain.model.User
import org.springframework.stereotype.Service

@Service
class UserService {
    private val users: MutableList<User> by lazy {
        mutableListOf(
            User(
                id = 1,
                name = "little_grasshopper99",
                email = "lgr@email.com",
            ),
        )
    }

    fun findById(id: Long): User = users.find { it.id == id } ?: throw Exception("User not found!")
}
