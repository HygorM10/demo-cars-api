package org.hygorm10.demoparkapi.exception

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(PasswordInvalidException::class)
    fun passwordInvalidException(
        exception: RuntimeException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorMessage> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                ErrorMessage(
                    request, HttpStatus.BAD_REQUEST,
                    exception.message!!
                )
            )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidException(
        exception: MethodArgumentNotValidException,
        request: HttpServletRequest,
        result: BindingResult
    ): ResponseEntity<ErrorMessage> {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).contentType(MediaType.APPLICATION_JSON)
            .body(ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, "Campo(s) inválidos", result))
    }

    @ExceptionHandler(UsernameUniqueViolationException::class)
    fun usernameUniqueViolationException(
        exception: UsernameUniqueViolationException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorMessage> {
        return ResponseEntity.status(HttpStatus.CONFLICT).contentType(MediaType.APPLICATION_JSON)
            .body(ErrorMessage(request, HttpStatus.CONFLICT, exception.messages))
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun entityNotFoundException(
        exception: EntityNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorMessage> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
            .body(ErrorMessage(request, HttpStatus.NOT_FOUND, exception.messages))
    }

}