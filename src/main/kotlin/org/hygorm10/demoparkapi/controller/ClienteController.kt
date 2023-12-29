package org.hygorm10.demoparkapi.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.hygorm10.demoparkapi.config.jwt.JwtUserDetail
import org.hygorm10.demoparkapi.controller.dto.ClienteCreateDto
import org.hygorm10.demoparkapi.controller.dto.ClienteResponseDto
import org.hygorm10.demoparkapi.entity.Cliente
import org.hygorm10.demoparkapi.service.ClienteService
import org.hygorm10.demoparkapi.service.UsuarioService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(
    name = "Clientes",
    description = "Contém todas as operações relativas aos recursos de cliente usuário."
)
@RestController
@RequestMapping("api/v1/clientes")
class ClienteController(
    val clienteService: ClienteService,
    val usuarioService: UsuarioService
) {

    @Operation(
        summary = "Cria um novo cliente",
        description = "Recurso para criar um novo cliente vinculado a um usuario cadastrado.",
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "Recurso criado com sucesso.",
                content = [Content(mediaType = "application/json")],
            ),
            ApiResponse(
                responseCode = "409",
                description = "Cliente CPF já cadastrado no sistema.",
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
    @PreAuthorize("hasRole('CLIENTE')")
    fun create(
        @RequestBody @Valid clienteDto: ClienteCreateDto,
        @AuthenticationPrincipal userDetails: JwtUserDetail
    ): ResponseEntity<ClienteResponseDto> {
        val cliente: Cliente = clienteDto.toCliente().copy(usuario = usuarioService.getById(userDetails.getId()))
        clienteService.create(cliente)
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente.toClienteResponseDto())
    }

    @Operation(
        summary = "Consultar cliente por ID",
        description = "Recurso para consultar cliente pelo ID.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Retornar cliente com sucesso.",
                content = [Content(mediaType = "application/json")],
            ),
            ApiResponse(
                responseCode = "404",
                description = "ID não cadastrado no sistema.",
                content = [Content(mediaType = "application/json")],
            ),
            ApiResponse(
                responseCode = "403",
                description = "Usuario não possui a permissão de ADMIN.",
                content = [Content(mediaType = "application/json")],
            )
        ]
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    fun findByID(@PathVariable id: Long): ResponseEntity<ClienteResponseDto> {
        return ResponseEntity.ok(clienteService.findByID(id).toClienteResponseDto())
    }


    @Operation(
        summary = "Consultar todos os clientes",
        description = "Recurso para consultar os clientes cadastrados.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Retornar clientes com sucesso.",
                content = [Content(mediaType = "application/json")],
            ),
            ApiResponse(
                responseCode = "403",
                description = "Usuario não possui a permissão de ADMIN.",
                content = [Content(mediaType = "application/json")],
            )
        ]
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    fun findAll(pageable: Pageable): ResponseEntity<Page<Cliente>> {
        return ResponseEntity.ok(clienteService.findAll(pageable))
    }

    @Operation(
        summary = "Consultar detalhes do cliente pelo token",
        description = "Recurso para consultar os dados do clientes pelo id do usuario.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Retornar dados cliente com sucesso.",
                content = [Content(mediaType = "application/json")],
            ),
            ApiResponse(
                responseCode = "403",
                description = "Usuario não possui a permissão de CLIENTE.",
                content = [Content(mediaType = "application/json")],
            )
        ]
    )
    @GetMapping("/detalhes")
    @PreAuthorize("hasRole('CLIENTE')")
    fun findDetails(@AuthenticationPrincipal jwt: JwtUserDetail): ResponseEntity<ClienteResponseDto> {
        return ResponseEntity.ok(clienteService.findByUserId(jwt.getId()).toClienteResponseDto())
    }

}