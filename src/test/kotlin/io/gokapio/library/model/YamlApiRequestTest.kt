package io.gokapio.library.model

import io.gokapio.library.error.InvalidRequestException
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.http.*

internal class YamlApiRequestTest : FunSpec({
    test("validate, no fields missing - do nothing") {
        shouldNotThrowAny {
            YamlApiRequest("name", HttpMethod.Get, "url", mapOf("header" to "value"), "body")
                .validate()
        }
    }

    test("validate, method null - throw invalid request exception") {
        shouldThrow<InvalidRequestException> {
            YamlApiRequest("name", HttpMethod(""), "url", mapOf("header" to "value"), "body")
                .validate()
        }.also { it.message shouldBe "Property value(s) [method] are missing." }
    }

    test("validate, url blank - throw invalid request exception") {
        shouldThrow<InvalidRequestException> {
            YamlApiRequest("name", HttpMethod.Put, "", mapOf("header" to "value"), "body")
                .validate()
        }.also { it.message shouldBe "Property value(s) [url] are missing." }
    }

    test("validate, all blank or null - throw exception for method and url") {
        shouldThrow<InvalidRequestException> {
            YamlApiRequest("", HttpMethod(""), "", null, "")
                .validate()
        }.also { it.message shouldBe "Property value(s) [method, url] are missing." }
    }
})
