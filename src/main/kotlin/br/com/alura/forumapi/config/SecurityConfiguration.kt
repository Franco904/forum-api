package br.com.alura.forumapi.config

import br.com.alura.forumapi.security.filter.JwtAuthFilter
import br.com.alura.forumapi.security.filter.JwtLoginFilter
import br.com.alura.forumapi.security.util.JwtUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val jwtUtil: JwtUtil,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                // Define roles for each endpoint
                authorize("/login", permitAll)

                // Only accept requests sent by authenticated users
                authorize(anyRequest, authenticated)
            }

            // Configure request filters
            addFilterBefore<UsernamePasswordAuthenticationFilter>(JwtLoginFilter(authenticationManager, jwtUtil))
            addFilterBefore<UsernamePasswordAuthenticationFilter>(JwtAuthFilter(jwtUtil))

            // Disable state saves between requests when logged in a user session
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
        }

        return http.build()
    }

    @Bean
    fun getPasswordEncoder() = BCryptPasswordEncoder()
}
