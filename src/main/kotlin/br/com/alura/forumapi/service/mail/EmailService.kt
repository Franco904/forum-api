package br.com.alura.forumapi.service.mail

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val mailSender: JavaMailSender,
) {
    fun notifyRecipient(targetEmail: String) {
        SimpleMailMessage().apply {
            subject = "Você recebeu uma resposta!"
            text = "Sua pergunta foi respondida, acesse o fórum do curso para conferir."
            setTo(targetEmail)
        }.also {
            mailSender.send(it)
        }
    }
}
