package org.hygorm10.demoparkapi.controller.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.hygorm10.demoparkapi.entity.Vaga
import org.hygorm10.demoparkapi.entity.enums.StatusVaga

data class VagaCreateDto(
    @field:NotBlank
    @field:Size(min = 4, max = 4)
    val codigo: String,
    @field:NotBlank
    @field:Pattern(regexp = "LIVRE|OCUPADA")
    val status: String
) {
    fun toVaga(): Vaga {
        return Vaga(
            codigo = this.codigo,
            status = StatusVaga.valueOf(this.status)
        )
    }
}
