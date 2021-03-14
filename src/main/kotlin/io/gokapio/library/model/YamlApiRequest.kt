package io.gokapio.library.model

import io.gokapio.library.error.InvalidRequestException
import io.ktor.http.*

data class YamlApiRequest(
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