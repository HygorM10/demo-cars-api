package org.hygorm10.demoparkapi.service

import org.hygorm10.demoparkapi.entity.Usuario
import org.hygorm10.demoparkapi.entity.enums.Role
import org.hygorm10.demoparkapi.exception.EntityNotFoundException
import org.hygorm10.demoparkapi.exception.PasswordInvalidException
import org.hygorm10.demoparkapi.exception.UsernameUniqueViolationException
import org.hygorm10.demoparkapi.repository.UsuarioRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UsuarioService(
    val usuarioRepository: UsuarioRepository,
    val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun create(usuario: Usuario): Usuario {
        try {
            return usuarioRepository.save(usuario.copy(password = passwordEncoder.encode(usuario.password)))
        } catch (ex: DataIntegrityViolationException) {
            throw UsernameUniqueViolationException("Username ${usuario.username} já cadastrado.")
        }

    }

    @Transactional(readOnly = true)
    fun getById(id: Long): Usuario {
        return usuarioRepository.findById(id)
            .orElseThrow { throw EntityNotFoundException("Usuário id=$id não encontrado.") }
    }

    @Transactional(readOnly = true)
    fun getUsers(): List<Usuario> {
        return usuarioRepository.findAll()
    }

    @Transactional
    fun updatePassword(id: Long, newPassword: String, oldPassword: String, newPasswordConfirmation: String): Usuario {
        if (newPassword != newPasswordConfirmation) throw PasswordInvalidException("As senhas não conferem.")
        val user = getById(id)
        if (!passwordEncoder.matches(
                oldPassword,
                user.password
            )
        ) throw PasswordInvalidException("Senha antiga incorreta.")
        return create(user.copy(password = passwordEncoder.encode(newPassword)))
    }

    @Transactional(readOnly = true)
    fun getByUsername(username: String): Usuario {
        return usuarioRepository.findByUsername(username)
            .orElseThrow { throw EntityNotFoundException("Usuário com username=$username não encontrado.") }
    }

    @Transactional(readOnly = true)
    fun getRoleByUsername(username: String): Role {
        return usuarioRepository.findRoleByUsername(username)
    }

}