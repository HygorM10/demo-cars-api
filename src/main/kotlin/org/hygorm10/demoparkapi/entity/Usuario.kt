package org.hygorm10.demoparkapi.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hygorm10.demoparkapi.controller.dto.UsuarioResponseDto
import org.hygorm10.demoparkapi.entity.enums.Role
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.LocalDateTime

@Entity
@Table(name = "usuarios")
@EntityListeners(AuditingEntityListener::class)
data class Usuario(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    val id: Long? = null,
    @Column(name = "username", nullable = false, unique = true, length = 100)
    val username: String,
    @Column(name = "password", nullable = false, length = 200)
    val password: String,
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 25)
    val role: Role,

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

    fun toUsuarioResponseDto(): UsuarioResponseDto {
        return UsuarioResponseDto(
            id = this.id!!,
            username = this.username,
            role = this.role.name.substring("ROLE_".length)
        )
    }

}