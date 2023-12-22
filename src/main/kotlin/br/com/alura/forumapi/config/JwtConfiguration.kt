package br.com.alura.forumapi.config

import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JwtConfiguration {
    @Bean
    fun getJwtBuilder(): JwtBuilder = Jwts.builder()

    @Bean
    fun getJwtParser(): JwtParser = Jwts.parser()
}
