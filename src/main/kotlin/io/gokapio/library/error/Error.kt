package io.gokapio.library.error

abstract class GokapioException(
    override val message: String?,
    override val cause: Throwable?
) : Exception(message, cause)

class YamlParserException(
    override val message: String?,
    override val cause: Throwable? = null
) : GokapioException(message, cause)

class InvalidRequestException(
    override val message: String?,
    override val cause: Throwable? = null
) : GokapioException(message, cause)

class HttpException(
    override val message: String?,
    override val cause: Throwable? = null
) : GokapioException(message, cause)