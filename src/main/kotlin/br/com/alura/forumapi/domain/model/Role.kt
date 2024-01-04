package br.com.alura.forumapi.domain.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.springframework.security.core.GrantedAuthority

@Entity(name = Role.TABLE_NAME)
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String,
) : GrantedAuthority {
    override fun getAuthority(): String = name

    companion object {
        const val TABLE_NAME = "roles"
    }
}
