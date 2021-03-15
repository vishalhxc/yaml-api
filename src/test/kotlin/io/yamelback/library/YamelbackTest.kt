package io.yamelback.library

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveAtLeastSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.yamelback.library.error.HttpException
import io.yamelback.library.error.InvalidRequestException
import io.yamelback.library.error.YamlParserException

internal class YamelbackTest : FunSpec({
    val yamelback = Yamelback()

    test("make request, from file to xkcd api, return reply") {
        yamelback.makeRequest("src/test/kotlin/io/yamelback/library/file/xkcd-get-comic-1.yaml")
            .let {
                it.code shouldBe 200
                it.status shouldBe "OK"
                it.content shouldBe """{"month": "1", "num": 1, "link": "", "year": "2006", "news": "", "safe_title": "Barrel - Part 1", "transcript": "[[A boy sits in a barrel which is floating in an ocean.]]\nBoy: I wonder where I'll float next?\n[[The barrel drifts into the distance. Nothing else can be seen.]]\n{{Alt: Don't we all.}}", "alt": "Don't we all.", "img": "https://imgs.xkcd.com/comics/barrel_cropped_(1).jpg", "title": "Barrel - Part 1", "day": "1"}"""
                it.headers shouldHaveAtLeastSize 1
            }
    }

    test("make request, from file to github api, return reply") {
        yamelback.makeRequest("src/test/kotlin/io/yamelback/library/file/github-get-vishalhxc.yaml")
            .let {
                it.code shouldBe 200
                it.status shouldBe "OK"
                it.content shouldContain "vishalhxc"
                it.content shouldContain "19310173"
                it.headers shouldHaveAtLeastSize 1
            }
    }

    test("make request, xkcd not found, return reply") {
        yamelback.makeRequest("src/test/kotlin/io/yamelback/library/file/xkcd-get-comic-not-found.yaml")
            .let {
                it.code shouldBe 404
                it.status shouldBe "Not Found"
            }
    }

    test("make request, from file missing fields, throw exception") {
        shouldThrow<InvalidRequestException> {
            yamelback.makeRequest("src/test/kotlin/io/yamelback/library/file/valid-file-missing-fields.yaml")
        }.also { it.message shouldBe "Property value(s) [url] are missing." }
    }

    test("make request, invalid file, throw exception") {
        shouldThrow<YamlParserException> {
            yamelback.makeRequest("src/test/kotlin/io/yamelback/library/file/invalid-file.yaml")
        }.also { it.message shouldBe "Invalid yaml file." }
    }

    test("make request, invalid request, throw exception") {
        shouldThrow<HttpException> {
            yamelback.makeRequest("src/test/kotlin/io/yamelback/library/file/invalid-request.yaml")
        }.also { it.message shouldBe "Could not complete request." }
    }
})