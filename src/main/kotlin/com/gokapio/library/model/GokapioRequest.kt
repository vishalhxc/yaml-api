package com.gokapio.library.model

import com.gokapio.library.error.InvalidRequestException
import io.ktor.http.*

data class GokapioRequest(
    val name: String,
    val method: HttpMethod?,
    val url: String,
    val headers: Map<String, String>?,
    val body: String
) {
    fun validate(): Unit = mutableListOf<String>().let { errors ->
        if (method == null) errors.add("method")
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