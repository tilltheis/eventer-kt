package eventer

import java.time.LocalDateTime

data class ListEvents(val events: List<Event>)

data class EditEvent(val action: String, val event: Event, val users: List<User>)

data class User(val id: String, val name: String)

data class Event(
    val id: String,
    val title: String,
    val host: User,
    val dateTime: LocalDateTime,
    val guests: List<User>,
    val description: String
)
