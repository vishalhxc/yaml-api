package io.yamelback.library.http

import io.yamelback.library.error.HttpException
import io.yamelback.library.http.model.HttpCall
import io.yamelback.library.http.model.HttpReply
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import java.lang.Exception

suspend fun sendHttpRequest(request: HttpCall, client: HttpClient = HttpClient()): HttpReply =
    try {
        client.request(request.url) {
            method = request.method
            body = request.body
            request.headers?.map { header(it.key, it.value) }
        }
    } catch (ex: Exception) {
        if (ex !is ResponseException)
            throw HttpException("Could not complete request.", ex)
        else ex.response
    }.run { HttpReply(status.value, status.description, readText(), headers.toListOfMap(), request) }

internal fun Headers.toListOfMap(): List<Map<String, String>> =
    this.toMap().map { mapOf(it.key to it.value.joinToString(separator = "; ")) }