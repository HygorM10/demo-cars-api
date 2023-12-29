package org.hygorm10.demoparkapi.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.hygorm10.demoparkapi.config.jwt.JwtUserDetailService
import org.hygorm10.demoparkapi.controller.dto.UsuarioLoginDto
import org.hygorm10.demoparkapi.exception.ErrorMessage
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Autenticação", description = "Recursos para proceder com a autenticação na API")
@RestController
@RequestMapping("/api/v1")
class AutenticacaoController(
    val detailsService: JwtUserDetailService,
    val authenticationManager: AuthenticationManager
) {

    @Operation(
        summary = "Autenticar na API", description = "Recurso de autenticação na API.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Autenticação realizada com sucesso e retorno de um berear token.",
                content = [Content(mediaType = "application/json")],
            ),
            ApiResponse(
                responseCode = "400",
                description = "Credenciais inválidas.",
                content = [Content(mediaType = "application/json")],
            ),
            ApiResponse(
                responseCode = "422",
                description = "Campo(s) inválido(s).",
                content = [Content(mediaType = "application/json")],
            )
        ]
    )
    @PostMapping("/auth")
    fun autenticar(
        @RequestBody @Valid usuarioLogin: UsuarioLoginDto,
        request: HttpServletRequest
    ): ResponseEntity<Any> {
        try {
            val authenticationToken = UsernamePasswordAuthenticationToken(usuarioLogin.username, usuarioLogin.password)
            authenticationManager.authenticate(authenticationToken)

            val jwtToken = detailsService.getTokenAuthenticated(usuarioLogin.username)

            return ResponseEntity.ok(jwtToken)
        } catch (ex: AuthenticationException) {
            println("Erro de autenticação do usermame ${usuarioLogin.username}: ${ex.message}")
        }

        return ResponseEntity.badRequest().body(ErrorMessage(request, BAD_REQUEST, "Credentials invalids"))
    }

}