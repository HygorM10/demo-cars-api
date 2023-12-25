package org.hygorm10.demoparkapi.config.jwt

import org.hygorm10.demoparkapi.entity.Usuario
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.User

class JwtUserDetail(
    private val usuario: Usuario
) : User(usuario.username, usuario.password, AuthorityUtils.createAuthorityList(usuario.role.name)) {

    fun getId(): Long {
        return usuario.id!!
    }

    fun getRole(): String {
        return usuario.role.name
    }

}