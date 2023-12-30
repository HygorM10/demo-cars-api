package org.hygorm10.demoparkapi.repository

import org.hygorm10.demoparkapi.entity.Vaga
import org.hygorm10.demoparkapi.entity.enums.StatusVaga
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional

interface VagaRepository : JpaRepository<Vaga, Long> {
    fun findByCodigo(codigo: String): Optional<Vaga>
    fun findFirstByStatus(livre: StatusVaga): Optional<Vaga>

    @Modifying
    @Query("update Vaga v set v.status = :status where v.id = :id")
    fun update(@Param("status") status: StatusVaga, @Param("id") id: Long)
}