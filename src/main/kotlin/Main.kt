package eventer

import io.ktor.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.concurrent.TimeUnit

fun main(args: Array<String>): Unit {
    val config = Config.load().eventer

    val db = Database.connect(
        config.dataSource.url,
        config.dataSource.driver,
        config.dataSource.username,
        config.dataSource.password
    )

    val now = transaction(db) {
        this.exec("select now()") {
            it.next()
            it.getString(1)
        }
    }

    val server = embeddedServer(
        Netty,
        port = config.webServer.port,
        watchPaths = listOf("eventer"),
        module = Application::main
    )
    server.start()

    println("Listening on port ${config.webServer.port}. Press ENTER to stop server.")
    readLine()

    server.stop(1, 1, TimeUnit.SECONDS)
}


