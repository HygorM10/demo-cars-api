package org.hygorm10.demoparkapi.service

import org.hygorm10.demoparkapi.entity.Vaga
import org.hygorm10.demoparkapi.exception.CodigoUniqueViolationException
import org.hygorm10.demoparkapi.exception.EntityNotFoundException
import org.hygorm10.demoparkapi.repository.VagaRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class VagaService(
    val vagaRepository: VagaRepository
) {

    @Transactional
    fun create(vaga: Vaga): Vaga {
        try {
            return vagaRepository.save(vaga)
        } catch (e: DataIntegrityViolationException) {
            throw CodigoUniqueViolationException("Vaga com codigo ${vaga.codigo} já cadastrada")
        }
    }

    @Transactional(readOnly = true)
    fun findByCodigo(codigo: String): Vaga {
        return vagaRepository.findByCodigo(codigo).orElseThrow {
            throw EntityNotFoundException("Vaga com codigo $codigo não encontrada")
        }
    }

}