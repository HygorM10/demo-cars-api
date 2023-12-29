package org.hygorm10.demoparkapi.controller.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.br.CPF
import org.hygorm10.demoparkapi.entity.Cliente

data class ClienteCreateDto(
    @field:NotBlank
    @field:Size(min = 5, max = 100)
    val nome: String,
    @field:Size(min = 11, max = 11)
    @field:CPF
    val cpf: String,
) {
    fun toCliente(): Cliente {
        return Cliente(
            nome = this.nome,
            cpf = this.cpf,
        )
    }
}
