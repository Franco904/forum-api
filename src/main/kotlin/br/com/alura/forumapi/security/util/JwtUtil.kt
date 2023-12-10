package br.com.alura.forumapi.security.util

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
object JwtUtil {
    private const val EXPIRATION_IN_MILLIS: Long = 60000 // 1 min
    @Value("\${jwt.secret}")
    private lateinit var secret: String

    fun generateToken(username: String): String? {
        return Jwts
            .builder()
            .setSubject(username)
            .setExpiration(Date(System.currentTimeMillis().plus(EXPIRATION_IN_MILLIS)))
            .signWith(SignatureAlgorithm.HS256, secret.toByteArray())
            .compact()
    }
}
