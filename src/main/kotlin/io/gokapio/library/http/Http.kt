package io.gokapio.library.http

import io.gokapio.library.error.HttpException
import io.gokapio.library.model.YamlApiRequest
import io.gokapio.library.model.YamlApiResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.util.date.*
import io.ktor.utils.io.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

suspend fun sendHttpRequest(request: YamlApiRequest, client: HttpClient = HttpClient()): YamlApiResponse =
    try {
        client.request(request.url) {
            method = request.method
            body = request.body
            request.headers?.map { header(it.key, it.value) }
        }
    } catch (ex: Exception) {
        if (ex !is ResponseException)
            throw HttpException("Unhandled exception on request.", ex)
        else ex.response
    }.run { YamlApiResponse(status.value, status.description, readText(), headers.toListOfMap()) }

internal fun Headers.toListOfMap(): List<Map<String, String>> =
    this.toMap().map { mapOf(it.key to it.value.joinToString(separator = "; ")) }