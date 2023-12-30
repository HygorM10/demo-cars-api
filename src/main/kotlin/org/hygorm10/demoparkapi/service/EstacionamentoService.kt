package org.hygorm10.demoparkapi.service

import org.hygorm10.demoparkapi.entity.Cliente
import org.hygorm10.demoparkapi.entity.ClienteVaga
import org.hygorm10.demoparkapi.entity.Vaga
import org.hygorm10.demoparkapi.entity.enums.StatusVaga.LIVRE
import org.hygorm10.demoparkapi.entity.enums.StatusVaga.OCUPADA
import org.hygorm10.demoparkapi.utils.EstacionamentoUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class EstacionamentoService(
    val clienteVagaService: ClienteVagaService,
    val clienteService: ClienteService,
    val vagaService: VagaService
) {

    @Transactional
    fun checkIn(clienteVaga: ClienteVaga): ClienteVaga {
        val cliente: Cliente = clienteService.findByCpf(clienteVaga.cliente.cpf)
        val vaga: Vaga = vagaService.findByVagaLivre()
        val newClienteVaga: ClienteVaga = clienteVaga.copy(cliente = cliente, vaga = vaga.copy(status = OCUPADA))
        return clienteVagaService.create(newClienteVaga)
    }

    @Transactional
    fun checkOut(recibo: String): ClienteVaga {
        val clienteVaga = clienteVagaService.findByRecibo(recibo)

        val dataSaida = LocalDateTime.now()

        val valor = EstacionamentoUtils.calcularCusto(clienteVaga.dataEntrada, dataSaida)

        val totalDeVezes = clienteVagaService.findTotalDeVezesEstacionamentoCompleto(clienteVaga.cliente.cpf)
        val desconto = EstacionamentoUtils.calcularDesconto(valor, totalDeVezes)

        val clienteVagaAtualizada = clienteVaga.copy(
            dataSaida = dataSaida,
            valor = valor,
            desconto = desconto
        )

        vagaService.update(status = LIVRE, id = clienteVaga.vaga?.id!!)

        return clienteVagaService.create(clienteVagaAtualizada)
    }

}