package br.com.alura.forumapi.domain.model

import jakarta.persistence.*

@Entity(name = User.TABLE_NAME)
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String,
    val email: String,
    val password: String,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "users_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    val roles: List<Role> = mutableListOf(),
) {
    companion object {
        const val TABLE_NAME = "users"
    }
}
