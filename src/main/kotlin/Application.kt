package eventer

import com.github.mustachejava.DefaultMustacheFactory
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.*
import io.ktor.features.CallLogging
import io.ktor.mustache.Mustache
import io.ktor.mustache.MustacheContent
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.sessions.*
import io.ktor.util.hex
import org.mindrot.jbcrypt.BCrypt
import java.lang.IllegalStateException
import java.time.LocalDateTime
import java.util.*

inline class SessionId(val id: String)

fun Application.main() {
    val config = Config.load().eventer
    val db = databaseModule(config.database)

    val sessionStore = mutableMapOf<SessionId, Login>()

    install(CallLogging)
    install(Mustache) {
        mustacheFactory = DefaultMustacheFactory("templates")
    }
    install(Sessions) {
        cookie<SessionId>("SESSIONID", SessionStorageMemory()) {
            val key = hex(config.session.signingKey)
            transform(SessionTransportTransformerMessageAuthentication(key))
        }
    }

    authentication {
        form {
            userParamName = "email"
            passwordParamName = "password"

            challenge {
                val errors: Map<Any, AuthenticationFailedCause> = call.authentication.errors
                when (errors.values.singleOrNull()) {
                    AuthenticationFailedCause.InvalidCredentials -> call.respondRedirect("/login?invalid")
                    else -> call.respondRedirect("/login")
                }
            }

            validate { credential: UserPasswordCredential ->
                LOGINS_BY_EMAIL[credential.name]?.let {
                    if (BCrypt.checkpw(credential.password, it.password)) UserIdPrincipal(it.id.id) else null
                }
            }

            skipWhen {
                it.sessions.get<SessionId>() in sessionStore.keys
            }
        }
    }

    routing {
        get("/login") {
            call.respond(MustacheContent("login.hbs", null))
        }

        authenticate {
            post("/login") {
                val principal = call.authentication.principal<UserIdPrincipal>()
                    ?: throw IllegalStateException("Missing principal right after login.")
                val sessionId = SessionId(UUID.randomUUID().toString())
                val loginId = LoginId(principal.name)
                val login = LOGINS_BY_ID[loginId] ?: throw IllegalStateException("No Login found for $loginId.")

                call.sessions.set(sessionId)
                sessionStore[sessionId] = login

                call.respondRedirect("/")
            }

            get("/secret") {
                call.respondText("secret")
            }
        }

        get("/") {
            val viewModel = ListEvents(
                listOf(
                    Event(
                        id = "id",
                        title = "title",
                        host = User("0", "host"),
                        dateTime = LocalDateTime.of(2019, 9, 23, 2, 21),
                        guests = listOf(User("1", "guest1"), User("2", "guest2")),
                        description = "description"
                    )
                )
            )
            call.respond(MustacheContent("listEvents.hbs", viewModel))
        }

        get("/events/new") {
            val viewModel = EditEvent(
                "Create Event",
                Event(
                    id = "id",
                    title = "title",
                    host = User("0", "host"),
                    dateTime = LocalDateTime.of(2019, 9, 23, 2, 21),
                    guests = listOf(User("1", "guest1"), User("2", "guest2")),
                    description = "description"
                ),
                listOf(User("3", "user3"), User("4", "user4"))
            )
            call.respond(MustacheContent("editEvent.hbs", viewModel))
        }

        get("/events/{id}") {
            val viewModel = EditEvent(
                "Update Event",
                Event(
                    id = "id",
                    title = "title",
                    host = User("0", "host"),
                    dateTime = LocalDateTime.of(2019, 9, 23, 2, 21),
                    guests = listOf(User("1", "guest1"), User("2", "guest2")),
                    description = "description"
                ),
                listOf(User("3", "user3"), User("4", "user4"))
            )
            call.respond(MustacheContent("editEvent.hbs", viewModel))
        }
    }
}
