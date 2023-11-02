package br.com.alura.forumapi.exception.classes

data class NotFoundException(override val message: String) : Exception()
