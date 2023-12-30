package org.hygorm10.demoparkapi.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hygorm10.demoparkapi.controller.dto.ClienteResponseDto
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.LocalDateTime

@Entity
@Table(name = "clientes")
@EntityListeners(AuditingEntityListener::class)
data class Cliente(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long? = null,
    @Column(name = "nome", nullable = false, length = 100)
    val nome: String? = null,
    @Column(name = "cpf", nullable = false, unique = true, length = 11)
    val cpf: String,
    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    val usuario: Usuario? = null,

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

    fun toClienteResponseDto(): ClienteResponseDto {
        return ClienteResponseDto(
            id = this.id!!,
            nome = this.nome!!,
            cpf = this.cpf,
        )
    }
}