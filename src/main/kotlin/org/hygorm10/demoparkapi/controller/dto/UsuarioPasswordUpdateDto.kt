package org.hygorm10.demoparkapi.controller.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UsuarioPasswordUpdateDto(
    @field:NotBlank(message = "Senha não pode ser vazia")
    @field:Size(min = 6, max = 6, message = "Senha deve ter 6 caracteres")
    val oldPassword: String,
    @field:NotBlank(message = "Senha não pode ser vazia")
    @field:Size(min = 6, max = 6, message = "Senha deve ter 6 caracteres")
    val newPassword: String,
    @field:NotBlank(message = "Senha não pode ser vazia")
    @field:Size(min = 6, max = 6, message = "Senha deve ter 6 caracteres")
    val newPasswordConfirmation: String
)
