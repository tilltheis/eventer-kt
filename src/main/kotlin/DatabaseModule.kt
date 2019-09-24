package eventer

import mu.KotlinLogging
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

private val logger = KotlinLogging.logger {}

fun databaseModule(config: Config.Companion.Database): Database {
    val flyway = Flyway.configure().dataSource(config.url, config.username, config.password).load()
    flyway.migrate()

    val db = Database.connect(config.url, config.driver, config.username, config.password)

    val now = transaction(db) {
        this.exec("select now()") {
            it.next()
            it.getString(1)
        }
    }
    logger.info { "now() = $now" }

    return db
}
