package org.hygorm10.demoparkapi.repository

import org.hygorm10.demoparkapi.entity.ClienteVaga
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface ClienteVagaRepository : JpaRepository<ClienteVaga, Long> {
    fun findByReciboAndDataSaidaIsNull(recibo: String): Optional<ClienteVaga>
    fun countByClienteCpfAndDataSaidaIsNotNull(cpf: String): Long
}