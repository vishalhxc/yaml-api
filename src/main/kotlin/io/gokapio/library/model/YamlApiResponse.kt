package io.gokapio.library.model

import io.ktor.http.*

data class YamlApiResponse(
    val code: Int,
    val status: String,
    val content: String,
    val headers: List<Map<String, String>>
)
