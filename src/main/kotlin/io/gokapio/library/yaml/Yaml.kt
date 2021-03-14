package io.gokapio.library.yaml

import io.gokapio.library.error.YamlParserException
import io.gokapio.library.model.YamlApiRequest
import io.ktor.http.*
import org.snakeyaml.engine.v2.api.Load
import org.snakeyaml.engine.v2.api.LoadSettings
import java.io.Reader

internal fun parseYaml(file: Reader): YamlApiRequest =
    Load(LoadSettings.builder().build()).loadFromReader(file).let { yaml ->
        if (yaml == null) throw YamlParserException("File does not exist.")
        if (yaml !is Map<*, *>) throw YamlParserException("Invalid yaml file.")
        yaml.toGokapioRequest()
    }

internal fun Map<*, *>.toGokapioRequest(): YamlApiRequest =
    YamlApiRequest(
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