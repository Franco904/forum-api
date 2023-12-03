package br.com.alura.forumapi.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfiguration {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                // Define roles for each endpoint
                authorize("/topics", hasAuthority("READ_WRITE"))

                // Only accept requests sent by authenticated users
                authorize(anyRequest, authenticated)
            }
            // Disable state saves between requests when logged in a user session
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
            // Disable login UI when in browser
            formLogin { disable() }
            // Use HTTP Basic authentication
            httpBasic {}
        }

        return http.build()
    }

    @Bean
    fun getPasswordEncoder() = BCryptPasswordEncoder()
}
