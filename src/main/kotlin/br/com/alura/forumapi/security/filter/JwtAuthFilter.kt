package br.com.alura.forumapi.security.filter

import br.com.alura.forumapi.security.util.JwtUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthFilter(
    private val jwtUtil: JwtUtil,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val authorizationHeaderPayload = request.getHeader(JwtUtil.AUTHORIZATION_HEADER)

        authorizationHeaderPayload?.let { headerPayload ->
            val token = jwtUtil.getTokenDetail(headerPayload)

            // If token is valid, user will remain authenticated & logged
            val auth = jwtUtil.getAuthenticationIfTokenIsValid(token)
            auth?.let {
                SecurityContextHolder.getContext().authentication = it
            }
        }

        filterChain.doFilter(request, response)
    }
}
