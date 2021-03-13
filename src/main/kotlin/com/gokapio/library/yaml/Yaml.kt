package com.gokapio.library.yaml

import com.gokapio.library.error.YamlParserException
import com.gokapio.library.model.GokapioRequest
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.snakeyaml.engine.v2.api.Load
import org.snakeyaml.engine.v2.api.LoadSettings
import java.io.Reader
import java.lang.Exception

internal fun parseYaml(file: Reader): GokapioRequest =
    Load(LoadSettings.builder().build()).loadFromReader(file).let { yaml ->
        if (yaml == null) throw YamlParserException("File does not exist.")
        if (yaml !is Map<*, *>) throw YamlParserException("Invalid yaml file.")
        yaml.toGokapioRequest()
    }

internal fun Map<*, *>.toGokapioRequest(): GokapioRequest =
    GokapioRequest(
        name = get("name")?.toString() ?: "",
        method = (get("method")?.toString() ?: "").toHttpMethod(),
        url = get("url")?.toString() ?: "",
        headers = get("headers")?.let {
            runCatching { (it as List<Map<String, String>>).merge() }
                .onFailure { throw YamlParserException("Unable to parse headers.") }
                .getOrNull()
        },
        body = get("body")?.toString() ?: ""
    )

internal fun String.toHttpMethod(): HttpMethod =
    HttpMethod.parse(this.trim().toUpperCase())

internal fun List<Map<String, String>>.merge(): Map<String, String> =
    mutableMapOf<String, String>().also { merged ->
        forEach { merged.putAll(it) }
    }