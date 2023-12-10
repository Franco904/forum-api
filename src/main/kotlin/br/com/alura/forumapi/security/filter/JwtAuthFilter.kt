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
        val rawToken = request.getHeader(JwtUtil.AUTH_HEADER) ?: return
        val token = jwtUtil.getTokenDetail(rawToken)

        jwtUtil.getAuthenticationIfTokenIsValid(token)?.let { authentication ->
            SecurityContextHolder.getContext().authentication = authentication
        }

        filterChain.doFilter(request, response)
    }
}
