package org.hygorm10.demoparkapi.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hygorm10.demoparkapi.controller.dto.VagaResponseDto
import org.hygorm10.demoparkapi.entity.enums.StatusVaga
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.LocalDateTime

@Entity
@Table(name = "vagas")
@EntityListeners(AuditingEntityListener::class)
data class Vaga(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "codigo", nullable = false, unique = true, length = 4)
    val codigo: String,
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    val status: StatusVaga,

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
    fun toVagaResponseDto(): VagaResponseDto {
        return VagaResponseDto(
            id = this.id!!,
            codigo = this.codigo,
            status = this.status.name
        )
    }
}
