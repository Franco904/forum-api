package br.com.alura.forumapi.domain.repository

import br.com.alura.forumapi.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>
