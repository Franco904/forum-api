package br.com.alura.forumapi.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl

@Configuration
class MailConfiguration {
    @Bean
    fun javaMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl().apply {
            host = "sandbox.smtp.mailtrap.io"
            port = 2525
            username = "65ad1a324d522a"
            password = "d0abd7c986017f"

            javaMailProperties["mail.transport.protocol"] = "smtp"
            javaMailProperties["mail.debug"] = "true"
            javaMailProperties["mail.smtp.auth"] = "true"
            javaMailProperties["mail.smtp.starttls.enable"] = "true"
        }

        return mailSender
    }
}
