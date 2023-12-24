package org.hygorm10.demoparkapi.service

import jakarta.transaction.Transactional
import org.hygorm10.demoparkapi.entity.Usuario
import org.hygorm10.demoparkapi.exception.EntityNotFoundException
import org.hygorm10.demoparkapi.exception.PasswordInvalidException
import org.hygorm10.demoparkapi.exception.UsernameUniqueViolationException
import org.hygorm10.demoparkapi.repository.UsuarioRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service

@Service
class UsuarioService(
    val usuarioRepository: UsuarioRepository
) {

    @Transactional
    fun create(usuario: Usuario): Usuario {
        try {
            return usuarioRepository.save(usuario)
        } catch (ex: DataIntegrityViolationException) {
            throw UsernameUniqueViolationException("Username ${usuario.username} já cadastrado.")
        }

    }

    @Transactional
    fun getById(id: Long): Usuario {
        return usuarioRepository.findById(id)
            .orElseThrow { throw EntityNotFoundException("Usuário id=$id não encontrado.") }
    }

    @Transactional
    fun getUsers(): List<Usuario> {
        return usuarioRepository.findAll()
    }

    @Transactional
    fun updatePassword(id: Long, newPassword: String, oldPassword: String, newPasswordConfirmation: String): Usuario {
        if (newPassword != newPasswordConfirmation) throw PasswordInvalidException("As senhas não conferem.")
        val user = getById(id)
        if (user.password != oldPassword) throw PasswordInvalidException("Senha antiga incorreta.")
        return create(user.copy(password = newPassword))
    }

}