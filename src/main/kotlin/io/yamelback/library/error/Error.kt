package io.yamelback.library.error

abstract class YamelbackException(
    override val message: String?,
    override val cause: Throwable?
) : Exception(message, cause)

class YamlParserException(
    override val message: String?,
    override val cause: Throwable? = null
) : YamelbackException(message, cause)

class InvalidRequestException(
    override val message: String?,
    override val cause: Throwable? = null
) : YamelbackException(message, cause)

class HttpException(
    override val message: String?,
    override val cause: Throwable? = null
) : YamelbackException(message, cause)