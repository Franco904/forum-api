package br.com.alura.forumapi.security.util

import br.com.alura.forumapi.service.UserService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtil(
    private val userService: UserService,
) {
    @Value("\${jwt.secret}")
    private lateinit var secret: String

    fun generateToken(username: String, authorities: MutableCollection<out GrantedAuthority>): String? {
        return Jwts.builder()
            .setSubject(username)
            .claim("role", authorities)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + EXPIRATION_IN_MILLIS))
            .signWith(SignatureAlgorithm.HS256, secret.toByteArray())
            .compact()
    }

    fun getTokenDetail(rawToken: String): String {
        val token = rawToken.split(BEARER_TOKEN_PREFIX)
        return if (token.size == 1) "" else token.component2()
    }

    fun getAuthenticationIfTokenIsValid(token: String): Authentication? {
        return try {
            val parseResult = Jwts.parser().setSigningKey(secret.toByteArray()).parseClaimsJws(token)

            val username = parseResult.body.subject
            val authorities = userService.loadUserByUsername(username).authorities

            UsernamePasswordAuthenticationToken(username, null, authorities)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
        const val BEARER_TOKEN_PREFIX = "Bearer "

        private const val EXPIRATION_IN_MILLIS: Long = 60000 // 1 min
    }
}
