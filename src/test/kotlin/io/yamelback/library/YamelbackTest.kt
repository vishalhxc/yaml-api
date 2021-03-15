package io.yamelback.library

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.http.*
import io.yamelback.library.http.model.HttpCall
import io.yamelback.library.http.model.HttpReply
import io.yamelback.library.util.constructYamlFile
import io.yamelback.library.util.mockClientWithResponse
import io.yamelback.library.yaml.parseYaml
import java.io.StringReader

internal class YamelbackTest : FunSpec({
    test("make request, parse yaml, validate, send request, return response") {
        val responseContent = """ { "eggs": "scrambled", "bread": "toasted" } """
        makeRequest(
            reader = constructYamlFile(
                name = "request-name",
                method = "POST",
                url = "http://localhost",
                headers = mapOf("content-type" to "application/json", "custom-header" to "custom-value"),
                body = """{ 
    "field" : "value",
    "numeric" : 30 
  }""".trimIndent()
            ),
            client = mockClientWithResponse(
                content = responseContent,
                status = HttpStatusCode.Created,
                headersOf("content-type" to listOf("application/json", "charset=utf-8"))
            )
        ) shouldBe HttpReply(
            code = 201,
            status = "Created",
            content = responseContent,
            headers = listOf(mapOf("content-type" to "application/json; charset=utf-8"))
        )
    }
})