package org.hygorm10.demoparkapi.service

import org.hygorm10.demoparkapi.entity.ClienteVaga
import org.hygorm10.demoparkapi.exception.EntityNotFoundException
import org.hygorm10.demoparkapi.repository.ClienteVagaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ClienteVagaService(
    val clienteVagaRepository: ClienteVagaRepository
) {

    @Transactional
    fun create(clienteVaga: ClienteVaga): ClienteVaga {
        return clienteVagaRepository.save(clienteVaga)
    }

    @Transactional(readOnly = true)
    fun findByRecibo(recibo: String): ClienteVaga {

        return clienteVagaRepository.findByReciboAndDataSaidaIsNull(recibo)
            .orElseThrow {
                throw EntityNotFoundException("Recibo $recibo não encontrado no sistema ou check-out já realizado.")
            }

    }

    @Transactional(readOnly = true)
    fun findTotalDeVezesEstacionamentoCompleto(cpf: String): Long {
        return clienteVagaRepository.countByClienteCpfAndDataSaidaIsNotNull(cpf)
    }

}