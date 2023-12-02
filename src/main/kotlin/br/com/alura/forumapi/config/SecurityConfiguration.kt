package br.com.alura.forumapi.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfiguration {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeHttpRequests { configurer ->
                // Aceita qualquer requisição advinda de um usuário autenticado
                configurer.anyRequest().authenticated()
            }
            .sessionManagement { configurer ->
                // Não guarda estado entre as requisições dentro da sessão do usuário
                configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .formLogin { configurer ->
                // Desabilita a interface visual de login no browser
                configurer.disable()
            }
            .httpBasic(withDefaults()) // Tipo da autenticação: basic
            .build()
    }

    @Bean
    fun getPasswordEncoder() = BCryptPasswordEncoder()
}
