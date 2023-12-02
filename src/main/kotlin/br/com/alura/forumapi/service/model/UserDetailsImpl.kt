package br.com.alura.forumapi.service.model

import br.com.alura.forumapi.domain.model.User
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl(
    private val user: User,
) : UserDetails {
    override fun getAuthorities() = user.roles

    override fun getPassword(): String = user.password

    override fun getUsername(): String = user.email

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true

    companion object {
        fun fromUser(user: User) = UserDetailsImpl(user)
    }
}
