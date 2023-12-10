package br.com.alura.forumapi.security.filter

import br.com.alura.forumapi.domain.model.Credentials
import br.com.alura.forumapi.security.util.JwtUtil
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class JwtLoginFilter(
    private val authManager: AuthenticationManager?,
    private val jwtUtil: JwtUtil,
) : UsernamePasswordAuthenticationFilter() {
    override fun attemptAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
    ): Authentication? {
        val requestBody = request?.inputStream
        val (username, password) = ObjectMapper().readValue(requestBody, Credentials::class.java)

        val token = UsernamePasswordAuthenticationToken(username, password)
        return authManager?.authenticate(token)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?,
    ) {
        val userDetails = authResult?.principal as UserDetails

        val token = jwtUtil.generateToken(userDetails.username)
        response?.addHeader(JwtUtil.AUTH_HEADER, "${JwtUtil.BEARER_TOKEN_PREFIX}$token")
    }
}
