package io.yamelback.library.http.model

data class HttpReply(
    val code: Int,
    val status: String,
    val content: String,
    val headers: List<Map<String, String>>
)
