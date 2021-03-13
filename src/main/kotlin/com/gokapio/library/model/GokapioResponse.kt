package com.gokapio.library.model

import io.ktor.http.*

data class GokapioResponse(
    val status: String,
    val payload: String,
)
