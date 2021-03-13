package com.gokapio.library.model

import com.gokapio.library.error.InvalidRequestException
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.http.*
import org.junit.jupiter.api.Assertions.*

internal class GokapioRequestTest : FunSpec({
    test("validate, no fields missing - do nothing") {
        shouldNotThrowAny {
            GokapioRequest("name", HttpMethod.Get, "url", mapOf("header" to "value"), "body")
                .validate()
        }
    }

    test("validate, method null - throw invalid request exception") {
        shouldThrow<InvalidRequestException> {
            GokapioRequest("name", null, "url", mapOf("header" to "value"), "body")
                .validate()
        }.also { it.message shouldBe "Property value(s) [method] are missing." }
    }

    test("validate, url blank - throw invalid request exception") {
        shouldThrow<InvalidRequestException> {
            GokapioRequest("name", HttpMethod.Put, "", mapOf("header" to "value"), "body")
                .validate()
        }.also { it.message shouldBe "Property value(s) [url] are missing." }
    }

    test("validate, all blank or null - throw exception for method and url") {
        shouldThrow<InvalidRequestException> {
            GokapioRequest("", null, "", null, "")
                .validate()
        }.also { it.message shouldBe "Property value(s) [method, url] are missing." }
    }
})
