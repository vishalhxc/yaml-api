package io.yamelback.library

import io.yamelback.library.error.HttpException
import io.yamelback.library.model.HttpCall
import io.yamelback.library.model.HttpReply
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.ktor.http.*
import io.yamelback.library.util.mockClientWithResponse

internal class HttpTest : FunSpec({
    test("send http request, valid get request, return 200 OK") {
        val call = HttpCall(
            name = "name",
            method = HttpMethod.Get,
            url = "http://localhost",
            headers = mapOf(
                "accept" to "application/json",
                "custom" to "custom-value"
            ),
            body = ""
        )
        val responseContent = """ { "eggs": "scrambled", "bread": "toasted" } """
        sendHttpRequest(
            request = call,
            client = mockClientWithResponse(
                responseContent,
                HttpStatusCode.OK,
                headersOf("content-type" to listOf("application/json", "charset=utf-8"))
            )
        ) shouldBe HttpReply(
            code = 200,
            status = "OK",
            content = responseContent,
            headers = listOf(mapOf("content-type" to "application/json; charset=utf-8")),
            httpCall = call
        )
    }

    test("send http request, valid post request, return 201 Created") {
        val call = HttpCall(
            name = "name",
            method = HttpMethod.Post,
            url = "http://localhost",
            headers = mapOf(
                "accept" to "application/json",
                "custom" to "custom-value"
            ),
            body = """ { "eggs": "scrambled", "bread": "toasted" } """
        )
        val responseContent = """ { "eggs": "scrambled", "bread": "toasted" } """

        sendHttpRequest(
            request = call,
            client = mockClientWithResponse(
                responseContent,
                HttpStatusCode.Created,
                headersOf("content-type" to listOf("application/json"))
            )
        ) shouldBe HttpReply(
            code = 201,
            status = "Created",
            content = responseContent,
            headers = listOf(mapOf("content-type" to "application/json")),
            httpCall = call
        )
    }

    test("send http request, invalid delete request, return 404 Not Found") {
        val call = HttpCall(
            name = "name",
            method = HttpMethod.Delete,
            url = "http://localhost",
            headers = emptyMap(),
            body = ""
        )
        val responseContent = """ { "error": "notFound" } """

        sendHttpRequest(
            request = call,
            client = mockClientWithResponse(
                responseContent,
                HttpStatusCode.NotFound,
                headersOf()
            )
        ) shouldBe HttpReply(
            code = 404,
            status = "Not Found",
            content = responseContent,
            headers = emptyList(),
            httpCall = call
        )
    }

    test("send http request, invalid put request, return 500 Internal Server Error") {
        val call = HttpCall(
            name = "name",
            method = HttpMethod.Put,
            url = "http://localhost",
            headers = emptyMap(),
            body = ""
        )
        val responseContent = """ { "error": "server error" } """

        sendHttpRequest(
            request = call,
            client = mockClientWithResponse(
                responseContent,
                HttpStatusCode.InternalServerError,
                headersOf()
            )
        ) shouldBe HttpReply(
            code = 500,
            status = "Internal Server Error",
            content = responseContent,
            headers = emptyList(),
            httpCall = call
        )
    }

    test("send http request, informational request, return 100") {
        val call = HttpCall(
            name = "name",
            method = HttpMethod.Put,
            url = "http://localhost",
            headers = emptyMap(),
            body = ""
        )
        sendHttpRequest(
            request = call,
            client = mockClientWithResponse(
                "",
                HttpStatusCode.Continue,
                headersOf()
            )
        ) shouldBe HttpReply(
            code = 100,
            status = "Continue",
            content = "",
            headers = emptyList(),
            httpCall = call
        )
    }

    test("send http request, redirect request, return 307") {
        val call = HttpCall(
            name = "name",
            method = HttpMethod.Put,
            url = "http://localhost",
            headers = emptyMap(),
            body = ""
        )
        sendHttpRequest(
            request = call,
            client = mockClientWithResponse(
                "",
                HttpStatusCode.TemporaryRedirect,
                headersOf()
            )
        ) shouldBe HttpReply(
            code = 307,
            status = "Temporary Redirect",
            content = "",
            headers = emptyList(),
            httpCall = call
        )
    }

    test("send http request, unreachable url, throw http exception with cause") {
        shouldThrow<HttpException> {
            sendHttpRequest(
                request = HttpCall(
                    name = "name",
                    method = HttpMethod.Put,
                    url = "http: // local host",
                    headers = emptyMap(),
                    body = ""
                )
            )
        }.also {
            it.message shouldBe "Could not complete request."
            it.cause shouldNotBe null
        }
    }

    test("to list of map, map of string/list, returns flattened") {
        headersOf(
            "one" to listOf("a", "b", "c"),
            "two" to listOf(),
            "three" to listOf("d", "e"),
            "four" to listOf("f"),
            "" to listOf("g", "h")
        ).toListOfMap() shouldBe listOf(
            mapOf("one" to "a; b; c"),
            mapOf("two" to ""),
            mapOf("three" to "d; e"),
            mapOf("four" to "f"),
            mapOf("" to "g; h")
        )
    }
})