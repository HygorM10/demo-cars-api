package org.hygorm10.demoparkapi.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
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

@Tag(
    name = "Usuários",
    description = "Contém todas as operações relativas aos recursos para cadastro, edição e leitura de um usuário."
)
@RestController
@RequestMapping("api/v1/usuarios")
class UsuarioController(
    val usuarioService: UsuarioService
) {

    @Operation(
        summary = "Cria um novo usuário", description = "Recurso para criar um novo usuário.",
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "Usuário criado com sucesso.",
                content = [Content(mediaType = "application/json")],
            ),
            ApiResponse(
                responseCode = "409",
                description = "Usuário e-mail já cadastrado no sistema.",
                content = [Content(mediaType = "application/json")],
            ),
            ApiResponse(
                responseCode = "422",
                description = "Recurso não processado por dados de entrada inválidos.",
                content = [Content(mediaType = "application/json")],
            )
        ]
    )
    @PostMapping
    fun create(@Valid @RequestBody createDto: UsuarioCreateDto): ResponseEntity<UsuarioResponseDto> {
        val usuario = usuarioService.create(createDto.toUsuario())
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario.toUsuarioResponseDto())
    }

    @Operation(
        summary = "Buscar um usuário por ID", description = "Recurso para procurar um usuário por id.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Recurso recuperado com sucesso.",
                content = [Content(mediaType = "application/json")],
            ),
            ApiResponse(
                responseCode = "404",
                description = "Recurso não encontrado.",
                content = [Content(mediaType = "application/json")],
            )
        ]
    )
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<UsuarioResponseDto> {
        val usuario = usuarioService.getById(id)
        return ResponseEntity.ok(usuario.toUsuarioResponseDto())
    }

    @Operation(
        summary = "Buscar todos os usuários",
        description = "Recurso para buscar todos os usuários cadastrados no sistema.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Recursos recuperados com sucesso.",
                content = [Content(mediaType = "application/json")],
            )
        ]
    )
    @GetMapping
    fun getUsers(): ResponseEntity<List<UsuarioResponseDto>> {
        val usuarios = usuarioService.getUsers().map { it.toUsuarioResponseDto() }
        return ResponseEntity.ok(usuarios)
    }

    @Operation(
        summary = "Atualizar senha de usuário", description = "Recurso para atualizar a senha do usuário por ID.",
        responses = [
            ApiResponse(
                responseCode = "204",
                description = "Senha atualizada com sucesso.",
                content = [Content(mediaType = "application/json")],
            ),
            ApiResponse(
                responseCode = "400",
                description = "Senha não confere.",
                content = [Content(mediaType = "application/json")],
            ),
            ApiResponse(
                responseCode = "404",
                description = "Recurso não encontrado.",
                content = [Content(mediaType = "application/json")],
            ),
            ApiResponse(
                responseCode = "422",
                description = "Senha antiga inválida.",
                content = [Content(mediaType = "application/json")],
            )
        ]
    )
    @PatchMapping("/{id}")
    fun updatePassword(
        @PathVariable id: Long,
        @Valid @RequestBody dto: UsuarioPasswordUpdateDto
    ): ResponseEntity<Void> {
        usuarioService.updatePassword(id, dto.newPassword, dto.oldPassword, dto.newPasswordConfirmation)
        return ResponseEntity.noContent().build()
    }

}