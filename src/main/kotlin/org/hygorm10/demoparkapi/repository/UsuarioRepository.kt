package org.hygorm10.demoparkapi.repository;

import org.hygorm10.demoparkapi.entity.Usuario
import org.hygorm10.demoparkapi.entity.enums.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface UsuarioRepository : JpaRepository<Usuario, Long> {

    fun findByUsername(username: String): Optional<Usuario>

    @Query("SELECT u.role FROM Usuario u WHERE u.username LIKE :username")
    fun findRoleByUsername(username: String): Role

}