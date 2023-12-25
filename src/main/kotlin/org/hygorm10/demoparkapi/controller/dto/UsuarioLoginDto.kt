package org.hygorm10.demoparkapi.controller.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UsuarioLoginDto(
    @field:NotBlank(message = "Nome de usuário não pode ser vazio")
    @field:Email(message = "Email inválido")
    val username: String,
    @field:NotBlank(message = "Senha não pode ser vazia")
    @field:Size(min = 6, max = 6, message = "Senha deve ter 6 caracteres")
    val password: String
)