package io.yamelback.library

import io.yamelback.library.error.YamlParserException
import io.yamelback.library.model.HttpCall
import io.ktor.http.*
import org.snakeyaml.engine.v2.api.Load
import org.snakeyaml.engine.v2.api.LoadSettings
import java.io.Reader

fun parseHttpCall(reader: Reader): HttpCall =
    Load(LoadSettings.builder().build()).loadFromReader(reader).let { yaml ->
        if (yaml == null) throw YamlParserException("File does not exist.")
        if (yaml !is Map<*, *>) throw YamlParserException("Invalid yaml file.")
        yaml.toHttpCall()
    }

internal fun Map<*, *>.toHttpCall(): HttpCall =
    HttpCall(
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