package org.hygorm10.demoparkapi.service

import org.hygorm10.demoparkapi.entity.Vaga
import org.hygorm10.demoparkapi.entity.enums.StatusVaga
import org.hygorm10.demoparkapi.entity.enums.StatusVaga.LIVRE
import org.hygorm10.demoparkapi.entity.enums.StatusVaga.OCUPADA
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

    @Transactional
    fun findByVagaLivre(): Vaga {
        val vagaLivre = vagaRepository.findFirstByStatus(LIVRE).orElseThrow {
            throw EntityNotFoundException("Não há vagas livres")
        }
        update(status = OCUPADA, id = vagaLivre.id!!)
        return vagaLivre
    }
    
    @Transactional
    fun update(status: StatusVaga, id: Long) {
        vagaRepository.update(status = status, id = id)
    }

}