package org.hygorm10.demoparkapi.exception

data class UsernameUniqueViolationException(
    val messages: String,
) : RuntimeException(messages)