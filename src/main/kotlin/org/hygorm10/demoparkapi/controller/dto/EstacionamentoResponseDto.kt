package org.hygorm10.demoparkapi.controller.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class EstacionamentoResponseDto(
    val placa: String,
    val marca: String,
    val modelo: String,
    val cor: String,
    val clienteCpf: String,
    val recibo: String,
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    val dataEntrada: LocalDateTime,
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    val dataSaida: LocalDateTime? = null,
    val vagaCodigo: String? = null,
    val valor: BigDecimal? = null,
    val desconto: BigDecimal? = null,
)
