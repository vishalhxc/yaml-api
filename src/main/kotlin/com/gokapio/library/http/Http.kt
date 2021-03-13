package com.gokapio.library.http

import com.gokapio.library.model.GokapioRequest
import com.gokapio.library.model.GokapioResponse
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

suspend fun sendHttpRequest(request: GokapioRequest): GokapioResponse =
    HttpClient().request<HttpResponse> {
        url(request.url)
        method = request.method
        body = request.body
        request.headers?.forEach { header(it.key, it.value) }
    }.run { GokapioResponse(status.toString(), readText()) }