package org.hygorm10.demoparkapi.service

import org.hygorm10.demoparkapi.entity.Cliente
import org.hygorm10.demoparkapi.exception.CpfUniqueViolationException
import org.hygorm10.demoparkapi.exception.EntityNotFoundException
import org.hygorm10.demoparkapi.repository.ClienteRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ClienteService(
    val clienteRepository: ClienteRepository,
) {

    @Transactional
    fun create(cliente: Cliente): Cliente {
        try {
            return clienteRepository.save(cliente)
        } catch (e: DataIntegrityViolationException) {
            throw CpfUniqueViolationException("CPF ${cliente.cpf} não pode ser cadastrado, pois já existe no sistema")
        }
    }

    @Transactional(readOnly = true)
    fun findByID(id: Long): Cliente {
        return clienteRepository.findById(id).orElseThrow {
            throw EntityNotFoundException("Cliente com id $id não encontrado")
        }
    }

    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<Cliente> {
        return clienteRepository.findAll(pageable)
    }

    @Transactional(readOnly = true)
    fun findByUserId(id: Long): Cliente {
        return clienteRepository.findByUsuarioId(id);
    }

    @Transactional(readOnly = true)
    fun findByCpf(cpf: String): Cliente {
        return clienteRepository.findByCpf(cpf).orElseThrow {
            throw EntityNotFoundException("Cliente com CPF $cpf não encontrado")
        }
    }

}