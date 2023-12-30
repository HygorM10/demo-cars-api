package org.hygorm10.demoparkapi.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hygorm10.demoparkapi.controller.dto.EstacionamentoResponseDto
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "clientes_tem_vagas")
@EntityListeners(AuditingEntityListener::class)
data class ClienteVaga(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "numero_recibo", nullable = false, unique = true, length = 15)
    val recibo: String,
    @Column(name = "placa", nullable = false, length = 8)
    val placa: String,
    @Column(name = "marca", nullable = false, length = 45)
    val marca: String,
    @Column(name = "modelo", nullable = false, length = 45)
    val modelo: String,
    @Column(name = "cor", nullable = false, length = 45)
    val cor: String,
    @Column(name = "data_entrada", nullable = false)
    val dataEntrada: LocalDateTime,
    @Column(name = "data_saida")
    val dataSaida: LocalDateTime? = null,
    @Column(name = "valor", columnDefinition = "decimal(7,2)")
    val valor: BigDecimal? = null,
    @Column(name = "desconto", columnDefinition = "decimal(7,2)")
    val desconto: BigDecimal? = null,

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    val cliente: Cliente,
    @ManyToOne
    @JoinColumn(name = "id_vaga", nullable = false)
    val vaga: Vaga? = null,

    @CreatedDate
    @Column(name = "data_criacao")
    val dataCriacao: LocalDateTime? = null,
    @LastModifiedDate
    @Column(name = "data_modificacao")
    val dataModificacao: LocalDateTime? = null,
    @CreatedBy
    @Column(name = "criado_por")
    val criadoPor: String? = null,
    @LastModifiedBy
    @Column(name = "modificado_por")
    val modificadoPor: String? = null
) : Serializable {

    fun toEstacionamentoResponseDto(): EstacionamentoResponseDto {
        return EstacionamentoResponseDto(
            placa = this.placa,
            marca = this.marca,
            modelo = this.modelo,
            cor = this.cor,
            clienteCpf = this.cliente.cpf,
            recibo = this.recibo,
            dataEntrada = this.dataEntrada,
            dataSaida = this.dataSaida,
            vagaCodigo = this.vaga!!.codigo,
            valor = this.valor,
            desconto = this.desconto
        )
    }

}
