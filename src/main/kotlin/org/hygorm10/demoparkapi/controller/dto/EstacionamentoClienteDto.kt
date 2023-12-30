package org.hygorm10.demoparkapi.controller.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.br.CPF
import org.hygorm10.demoparkapi.entity.Cliente
import org.hygorm10.demoparkapi.entity.ClienteVaga
import org.hygorm10.demoparkapi.utils.EstacionamentoUtils
import java.time.LocalDateTime

data class EstacionamentoClienteDto(
    @field:NotBlank
    @field:Size(min = 8, max = 8)
    @field:Pattern(regexp = "[A-Z]{3}-[0-9]{4}", message = "A placa do veiculo deve seguir o padrão xxx-0000")
    val placa: String,
    @field:NotBlank
    val marca: String,
    @field:NotBlank
    val modelo: String,
    @field:NotBlank
    val cor: String,
    @field:NotBlank
    @field:Size(min = 11, max = 11)
    @field:CPF
    val clienteCpf: String,
) {
    fun toClienteVaga(): ClienteVaga {
        return ClienteVaga(
            placa = this.placa,
            marca = this.marca,
            modelo = this.modelo,
            cor = this.cor,
            dataEntrada = LocalDateTime.now(),
            cliente = Cliente(cpf = this.clienteCpf),
            recibo = EstacionamentoUtils.gerarRecibo()
        )
    }
}
