package br.com.alura.forumapi.domain.model

import jakarta.persistence.*

@Entity(name = "courses")
data class Course(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String,
    val category: String,
)
