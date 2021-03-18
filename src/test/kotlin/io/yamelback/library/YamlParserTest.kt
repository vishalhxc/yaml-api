package io.yamelback.library

import io.yamelback.library.error.YamlParserException
import io.yamelback.library.model.HttpCall
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.http.*
import io.yamelback.library.util.constructYamlFile
import java.io.Reader
import java.io.StringReader

internal class YamlParserTest : FunSpec({
    test("parse yaml, reader has valid yaml, return request") {
        parseHttpCall(
            constructYamlFile(
                name = "request-name",
                method = "GET",
                url = "http://localhost",
                headers = mapOf("content-type" to "application/json", "custom-header" to "custom-value"),
                body = """{ 
    "field" : "value",
    "numeric" : 30 
  }""".trimIndent()
            )
        ) shouldBe HttpCall(
            name = "request-name",
            method = HttpMethod.Get,
            url = "http://localhost",
            headers = mapOf(
                "content-type" to "application/json",
                "custom-header" to "custom-value"
            ),
            body = """{ 
  "field" : "value",
  "numeric" : 30 
}"""
        )
    }

    test("parse yaml, reader has valid yaml - wrong fields, return blank request") {
        parseHttpCall(StringReader("abc: 123")) shouldBe HttpCall(
            name = "",
            method = HttpMethod(""),
            url = "",
            headers = null,
            body = ""
        )
    }

    test("parse yaml, yaml not valid, throw yaml exception") {
        shouldThrow<YamlParserException> { parseHttpCall(StringReader("Some random text and that's it.")) }
            .also { it.message shouldBe "Invalid yaml file." }
    }

    test("parse yaml, yaml null, throw yaml exception") {
        shouldThrow<YamlParserException> { parseHttpCall(Reader.nullReader()) }
            .also { it.message shouldBe "File does not exist." }
    }

    test("to request, maps to values and returns request") {
        mapOf(
            "name" to "request-name",
            "method" to "get",
            "url" to "http://localhost",
            "headers" to listOf(
                mapOf("content-type" to "application/json"),
                mapOf("custom-header" to "custom-value")
            ),
            "body" to " { \"field\": \"value\", \"numeric\": 30 } "
        ).toHttpCall() shouldBe HttpCall(
            name = "request-name",
            method = HttpMethod.Get,
            url = "http://localhost",
            headers = mapOf(
                "content-type" to "application/json",
                "custom-header" to "custom-value"
            ),
            body = """ { "field": "value", "numeric": 30 } """
        )
    }

    test("to request, blank name - maps to values and returns request") {
        mapOf(
            "method" to "get",
            "url" to "http://localhost",
            "headers" to listOf(
                mapOf("content-type" to "application/json"),
                mapOf("custom-header" to "custom-value")
            ),
            "body" to " { \"field\": \"value\", \"numeric\": 30 } "
        ).toHttpCall() shouldBe HttpCall(
            name = "",
            method = HttpMethod.Get,
            url = "http://localhost",
            headers = mapOf(
                "content-type" to "application/json",
                "custom-header" to "custom-value"
            ),
            body = """ { "field": "value", "numeric": 30 } """
        )
    }

    test("to request, no headers - maps to null and returns request") {
        mapOf(
            "name" to "request-name",
            "method" to "get",
            "url" to "http://localhost",
            "body" to " { \"field\": \"value\", \"numeric\": 30 } "
        ).toHttpCall() shouldBe HttpCall(
            name = "request-name",
            method = HttpMethod.Get,
            url = "http://localhost",
            headers = null,
            body = """ { "field": "value", "numeric": 30 } """
        )
    }

    test("to request, invalid headers - throws exception") {
        shouldThrow<YamlParserException> {
            mapOf(
                "name" to "request-name",
                "method" to "get",
                "url" to "http://localhost",
                "headers" to "something-thats-not-a-map",
                "body" to " { \"field\": \"value\", \"numeric\": 30 } "
            ).toHttpCall() shouldBe HttpCall(
                name = "request-name",
                method = HttpMethod.Get,
                url = "http://localhost",
                headers = null,
                body = """ { "field": "value", "numeric": 30 } """
            )
        }.also { it.message shouldBe "Unable to parse headers." }
    }

    test("to request, missing method maps to empty and returns request") {
        mapOf(
            "url" to "http://localhost",
            "headers" to listOf(
                mapOf("content-type" to "application/json"),
                mapOf("custom-header" to "custom-value")
            ),
            "body" to " { \"field\": \"value\", \"numeric\": 30 } "
        ).toHttpCall() shouldBe HttpCall(
            name = "",
            method = HttpMethod(""),
            url = "http://localhost",
            headers = mapOf("content-type" to "application/json", "custom-header" to "custom-value"),
            body = """ { "field": "value", "numeric": 30 } """
        )
    }

    test("to request, missing body maps to blank") {
        mapOf(
            "name" to "request-name",
            "method" to "get",
            "url" to "http://localhost",
            "headers" to listOf(
                mapOf("content-type" to "application/json"),
                mapOf("custom-header" to "custom-value")
            ),
        ).toHttpCall() shouldBe HttpCall(
            name = "request-name",
            method = HttpMethod.Get,
            url = "http://localhost",
            headers = mapOf(
                "content-type" to "application/json",
                "custom-header" to "custom-value"
            ),
            body = ""
        )
    }

    test("to request, missing headers maps to null") {
        mapOf(
            "name" to "request-name",
            "method" to "get",
            "url" to "http://localhost",
            "body" to " { \"field\": \"value\", \"numeric\": 30 } "
        ).toHttpCall() shouldBe HttpCall(
            name = "request-name",
            method = HttpMethod.Get,
            url = "http://localhost",
            headers = null,
            body = """ { "field": "value", "numeric": 30 } """
        )
    }

    test("to http method, 'GET' returns Get method") {
        "GET".toHttpMethod() shouldBe HttpMethod.Get
    }

    test("to http method, 'get' returns Get method") {
        "get".toHttpMethod() shouldBe HttpMethod.Get
    }

    test("to http method, 'Get' returns Get method") {
        "Get".toHttpMethod() shouldBe HttpMethod.Get
    }

    test("to http method, ' gEt  ' returns Get method") {
        " gEt  ".toHttpMethod() shouldBe HttpMethod.Get
    }

    test("to http method, '   pOsT  ' returns Post method") {
        "   pOsT  ".toHttpMethod() shouldBe HttpMethod.Post
    }

    test("to http method, '  pUt  ' returns Put method") {
        "  pUt  ".toHttpMethod() shouldBe HttpMethod.Put
    }

    test("to http method, '   pAtCh  ' returns Patch method") {
        "   pAtCh  ".toHttpMethod() shouldBe HttpMethod.Patch
    }

    test("to http method, '   dElEtE   ' returns Patch method") {
        "   dElEtE   ".toHttpMethod() shouldBe HttpMethod.Delete
    }

    test("merge, list of maps returns single map") {
        listOf(
            mapOf("blah" to "abc"),
            mapOf("something" to "something-else", "another-thing" to "other"),
            mapOf(),
            mapOf("a" to "1", "b" to "2", "c" to "3")
        ).merge() shouldBe mapOf(
            "blah" to "abc",
            "something" to "something-else",
            "another-thing" to "other",
            "a" to "1",
            "b" to "2",
            "c" to "3"
        )
    }

})