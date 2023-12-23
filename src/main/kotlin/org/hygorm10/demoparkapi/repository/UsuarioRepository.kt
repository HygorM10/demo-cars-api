package org.hygorm10.demoparkapi.repository;

import org.hygorm10.demoparkapi.entity.Usuario
import org.springframework.data.jpa.repository.JpaRepository

interface UsuarioRepository : JpaRepository<Usuario, Long> {
}