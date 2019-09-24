package eventer

import io.ktor.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.util.concurrent.TimeUnit

fun main(args: Array<String>): Unit {
    val config = Config.load().eventer.webServer

    val server = embeddedServer(Netty, port = config.port, watchPaths = listOf("eventer"), module = Application::main)
    server.start()

    println("Listening on port ${config.port}. Press ENTER to stop server.")
    readLine()

    server.stop(1, 1, TimeUnit.SECONDS)
}
