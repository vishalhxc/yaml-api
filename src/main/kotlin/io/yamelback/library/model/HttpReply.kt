package io.yamelback.library.model

data class HttpReply(
    val code: Int,
    val status: String,
    val content: String,
    val headers: List<Map<String, String>>,
    val httpCall: HttpCall
)
