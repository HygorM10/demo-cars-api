package org.hygorm10.demoparkapi.config.jwt

import org.hygorm10.demoparkapi.entity.Usuario
import org.hygorm10.demoparkapi.entity.enums.Role
import org.hygorm10.demoparkapi.service.UsuarioService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class JwtUserDetailService(
    val usuarioService: UsuarioService
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val usuario: Usuario = usuarioService.getByUsername(username)
        return JwtUserDetail(usuario)
    }

    fun getTokenAuthenticated(username: String): JwtToken {
        val role: Role = usuarioService.getRoleByUsername(username)
        return JwtUtils.createToken(username, role.name.substring("ROLE_".length))
    }
}