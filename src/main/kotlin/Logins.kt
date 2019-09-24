package eventer

inline class LoginId(val id: String)
data class Login(val id: LoginId, val email: String, val password: String)

// password = "password"
val LOGINS = listOf(
    Login(
        LoginId("32ec5ce0-5173-4a52-8fc8-62356bd26cc5"),
        "till@example.org",
        "$2a$10\$d.vQEHwPIqtSYWQOMtg7LuZgTOx1R/2sOLnqCUkpixkXJ1paUhEIm"
    ),
    Login(
        LoginId("fae95a3e-7e9c-4bcc-8903-87305b055bfe"),
        "janni@example.org",
        "$2a$10\$d.vQEHwPIqtSYWQOMtg7LuZgTOx1R/2sOLnqCUkpixkXJ1paUhEIm"
    )
)

val LOGINS_BY_EMAIL = LOGINS.groupBy { it.email }.mapValues { it.value.first() }
val LOGINS_BY_ID = LOGINS.groupBy { it.id }.mapValues { it.value.first() }
