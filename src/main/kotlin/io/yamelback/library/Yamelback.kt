package io.yamelback.library

import io.ktor.client.*
import io.yamelback.library.http.model.HttpReply
import io.yamelback.library.http.sendHttpRequest
import io.yamelback.library.yaml.parseYaml
import java.io.File
import java.io.Reader

class Yamelback {
    suspend fun makeRequest(filename: String, client: HttpClient = HttpClient()) : HttpReply =
        parseYaml(File(filename).reader())
            .apply { validate() }
            .let { sendHttpRequest(it, client) }
}