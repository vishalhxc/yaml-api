package io.yamelback.library.model

import io.yamelback.library.error.InvalidRequestException
import io.ktor.http.*

data class HttpCall(
    val name: String,
    val method: HttpMethod,
    val url: String,
    val headers: Map<String, String>?,
    val body: String
) {
    fun validate(): Unit = mutableListOf<String>().let { errors ->
        if (method.value.isBlank()) errors.add("method")
        if (url.isBlank()) errors.add("url")
        if (errors.isNotEmpty())
            throw InvalidRequestException(
                errors.joinToString(
                    prefix = "Property value(s) [",
                    postfix = "] are missing.",
                    separator = ", "
                )
            )
    }
}