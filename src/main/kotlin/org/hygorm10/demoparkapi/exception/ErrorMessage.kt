package org.hygorm10.demoparkapi.exception

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult

data class ErrorMessage(
    val path: String,
    val method: String,
    val status: Int,
    val statusText: String,
    val message: String,
    @JsonInclude(NON_EMPTY)
    val errors: HashMap<String, String> = hashMapOf()
) {
    constructor(
        request: HttpServletRequest, status: HttpStatus, message: String
    ) : this(
        path = request.requestURI,
        method = request.method,
        status = status.value(),
        statusText = status.reasonPhrase,
        message = message
    )

    constructor(
        request: HttpServletRequest, status: HttpStatus, message: String, result: BindingResult
    ) : this(
        path = request.requestURI,
        method = request.method,
        status = status.value(),
        statusText = status.reasonPhrase,
        message = message
    ) {
        addError(result)
    }

    private fun addError(result: BindingResult) {
        for (error in result.fieldErrors) {
            this.errors[error.field] = error.defaultMessage ?: "Erro desconhecido"
        }
    }
}