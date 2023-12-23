package org.hygorm10.demoparkapi.exception

data class EntityNotFoundException(
    val messages: String,
) : RuntimeException(
    messages
)