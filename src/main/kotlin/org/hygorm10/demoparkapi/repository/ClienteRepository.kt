package org.hygorm10.demoparkapi.repository

import org.hygorm10.demoparkapi.entity.Cliente
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface ClienteRepository : JpaRepository<Cliente, Long> {
    fun findByUsuarioId(id: Long): Cliente
    fun findByCpf(cpf: String): Optional<Cliente>
}