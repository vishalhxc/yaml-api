package io.yamelback.library

import io.ktor.client.*
import io.yamelback.library.model.HttpReply
import java.io.File

class YamlHttpClient(private val client: HttpClient = HttpClient()) {
    suspend fun makeRequest(filename: String): HttpReply =
        parseHttpCall(File(filename).reader()).let { call ->
            call.validate()
            sendHttpRequest(call, client)
        }
}