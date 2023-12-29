package org.hygorm10.demoparkapi.repository

import org.hygorm10.demoparkapi.entity.Vaga
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface VagaRepository : JpaRepository<Vaga, Long> {
    fun findByCodigo(codigo: String): Optional<Vaga>
}