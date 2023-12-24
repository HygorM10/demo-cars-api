package org.hygorm10.demoparkapi.exception

data class PasswordInvalidException(
    val messages: String,
) : RuntimeException(
    messages
)