package shared.utils.jpa

import br.com.alura.forumapi.domain.model.Course
import br.com.alura.forumapi.domain.model.Role
import br.com.alura.forumapi.domain.model.User
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.jdbc.JdbcTestUtils

fun JdbcTemplate.populateDomainTables(
    courses: List<Course>,
    users: List<User>,
    roles: List<Role>,
) {
    for (course in courses) {
        update("""
            INSERT INTO courses(id, name, category)
            VALUES (${course.id}, '${course.name}', '${course.category}')
        """)
    }

    for (user in users) {
        update("""
            INSERT INTO users(id, name, email, password)
            VALUES (${user.id}, '${user.name}', '${user.email}', '${user.password}');
        """)
    }

    for ((i, role) in roles.withIndex()) {
        update("INSERT INTO roles(id, name) VALUES (${role.id}, '${role.name}');")
        update("INSERT INTO users_roles(id, user_id, role_id) VALUES ($i, ${users[i].id}, ${role.id});")
    }
}

fun JdbcTemplate.clearAllTables() {
    JdbcTestUtils.deleteFromTables(
        this,
        "courses", "users", "topics", "answers", "roles", "users_roles",
    )
}
