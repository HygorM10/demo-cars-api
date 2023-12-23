package org.hygorm10.demoparkapi.controller

import jakarta.validation.Valid
import org.hygorm10.demoparkapi.controller.dto.UsuarioCreateDto
import org.hygorm10.demoparkapi.controller.dto.UsuarioPasswordUpdateDto
import org.hygorm10.demoparkapi.controller.dto.UsuarioResponseDto
import org.hygorm10.demoparkapi.service.UsuarioService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/usuarios")
class UsuarioController(
    val usuarioService: UsuarioService
) {

    @PostMapping
    fun create(@Valid @RequestBody createDto: UsuarioCreateDto): ResponseEntity<UsuarioResponseDto> {
        val usuario = usuarioService.create(createDto.toUsuario())
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario.toUsuarioResponseDto())
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<UsuarioResponseDto> {
        val usuario = usuarioService.getById(id)
        return ResponseEntity.ok(usuario.toUsuarioResponseDto())
    }

    @GetMapping
    fun getUsers(): ResponseEntity<List<UsuarioResponseDto>> {
        val usuarios = usuarioService.getUsers().map { it.toUsuarioResponseDto() }
        return ResponseEntity.ok(usuarios)
    }

    @PatchMapping("/{id}")
    fun updatePassword(
        @PathVariable id: Long,
        @Valid @RequestBody dto: UsuarioPasswordUpdateDto
    ): ResponseEntity<Void> {
        usuarioService.updatePassword(id, dto.newPassword, dto.oldPassword, dto.newPasswordConfirmation)
        return ResponseEntity.noContent().build()
    }

}