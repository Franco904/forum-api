package br.com.alura.forumapi.security.util

import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtil(
    private val builder: JwtBuilder,
    private val parser: JwtParser,
) {
    @Value("\${jwt.secret}")
    private lateinit var secret: String

    fun generateToken(username: String): String? {
        return builder
            .setSubject(username)
            .setExpiration(Date(System.currentTimeMillis().plus(EXPIRATION_IN_MILLIS)))
            .signWith(SignatureAlgorithm.HS256, secret.toByteArray())
            .compact()
    }

    fun getTokenDetail(rawToken: String): String {
        val token = rawToken.split(BEARER_TOKEN_PREFIX)
        return if (token.size == 1) "" else token.component2()
    }

    fun getAuthenticationIfTokenIsValid(token: String): Authentication? {
        return try {
            val parseResult = parser.setSigningKey(secret.toByteArray()).parseClaimsJwt(token)
            val username = parseResult.body.subject

            UsernamePasswordAuthenticationToken(username, null)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    companion object {
        const val AUTH_HEADER = "Authorization"
        const val BEARER_TOKEN_PREFIX = "Bearer "

        private const val EXPIRATION_IN_MILLIS: Long = 60000 // 1 min
    }
}
