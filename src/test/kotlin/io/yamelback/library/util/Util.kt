package io.yamelback.library.util

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import java.io.Reader
import java.io.StringReader

internal fun mockClientWithResponse(content: String, status: HttpStatusCode, headers: Headers): HttpClient =
    HttpClient(MockEngine) {
        engine {
            addHandler { respond(content, status, headers) }
        }
    }

internal fun constructYamlFile(
    name: String,
    method: String,
    url: String,
    headers: Map<String, String>,
    body: String
): Reader = """
name: $name
method: $method
url: $url
headers:
${headers.map { "  - ${it.key}: ${it.value}" }.joinToString(separator = System.lineSeparator())}
body: >-
  $body
""".trimIndent().reader()