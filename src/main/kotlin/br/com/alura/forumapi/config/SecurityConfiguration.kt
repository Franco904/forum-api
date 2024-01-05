package br.com.alura.forumapi.config

import br.com.alura.forumapi.security.filter.JwtAuthFilter
import br.com.alura.forumapi.security.filter.JwtLoginFilter
import br.com.alura.forumapi.security.util.JwtUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.HttpSecurityDsl
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
    private val authConfig: AuthenticationConfiguration,
    private val jwtUtil: JwtUtil,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                // Set authorities (user roles) for each endpoint
                authorize(HttpMethod.POST, "/login", permitAll)
                authorize(HttpMethod.GET, "/swagger-ui/**", permitAll)
                authorize(HttpMethod.GET, "/v3/api-docs/**", permitAll)

                // Domain endpoints
                authorize("/topics", hasAuthority("READ_WRITE"))

                // Only accept requests sent by authenticated users
                authorize(anyRequest, authenticated)
            }

            // Disable CSRF to make JWT work properly
            csrf { disable() }

            // Disable state saves between requests when logged in a user session
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }

            // Intercept all incoming requests
            setUpFilterChain()
        }

        return http.build()
    }

    private fun HttpSecurityDsl.setUpFilterChain() {
        // Token generation filter
        addFilterBefore<UsernamePasswordAuthenticationFilter>(JwtLoginFilter(authConfig.authenticationManager, jwtUtil))

        // Endpoint authentication/authorization filter
        addFilterBefore<UsernamePasswordAuthenticationFilter>(JwtAuthFilter(jwtUtil))
    }

    @Bean
    fun getPasswordEncoder() = BCryptPasswordEncoder()
}
