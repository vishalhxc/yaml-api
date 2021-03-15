package io.yamelback.library

import io.ktor.client.*
import io.yamelback.library.http.model.HttpReply
import io.yamelback.library.http.sendHttpRequest
import io.yamelback.library.yaml.parseYaml
import java.io.Reader

suspend fun makeRequest(reader: Reader, client: HttpClient = HttpClient()) : HttpReply =
    parseYaml(reader)
        .apply { validate() }
        .let { sendHttpRequest(it, client) }