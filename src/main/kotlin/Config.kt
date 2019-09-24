package eventer

import com.sksamuel.hoplite.ConfigLoader

data class Config(val eventer: Eventer) {
    companion object {
        data class Eventer(val webServer: WebServer, val dataSource: DataSource, val session: Session)
        data class WebServer(val port: Int)
        data class DataSource(val url: String, val driver: String, val username: String, val password: String)
        data class Session(val signingKey: String)

        fun load(): Config = ConfigLoader().loadConfigOrThrow<Config>("/application.conf")
    }
}

