package org.hygorm10.demoparkapi.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.hygorm10.demoparkapi.controller.dto.VagaCreateDto
import org.hygorm10.demoparkapi.controller.dto.VagaResponseDto
import org.hygorm10.demoparkapi.service.VagaService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@Tag(
    name = "Vagas",
    description = "Contém todas as operações relativas aos recursos de vagas."
)
@RestController
@RequestMapping("/api/v1/vagas")
class VagaController(
    val vagaService: VagaService
) {

    @Operation(
        summary = "Cria uma nova vaga",
        description = "Recurso para criar uma nova vaga.",
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "Recurso criado com sucesso.",
                content = [Content(mediaType = "application/json")],
            ),
            ApiResponse(
                responseCode = "409",
                description = "Codigo da vaga já cadastrado.",
                content = [Content(mediaType = "application/json")],
            ),
            ApiResponse(
                responseCode = "403",
                description = "Usuario não possui a role ADMIN.",
                content = [Content(mediaType = "application/json")],
            )
        ]
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun create(@RequestBody @Valid dto: VagaCreateDto): ResponseEntity<Void> {
        vagaService.create(dto.toVaga())
        val localition: URI = ServletUriComponentsBuilder
            .fromCurrentRequestUri().path("/{codigo}")
            .buildAndExpand(dto.codigo).toUri()
        return ResponseEntity.created(localition).build()
    }

    @Operation(
        summary = "Consulta uma vaga",
        description = "Recurso para consultar uma vaga por código.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Recurso encontrado com sucesso.",
                content = [Content(mediaType = "application/json")],
            ),
            ApiResponse(
                responseCode = "404",
                description = "Vaga não encontrada.",
                content = [Content(mediaType = "application/json")],
            ),
            ApiResponse(
                responseCode = "403",
                description = "Usuario não possui a role ADMIN.",
                content = [Content(mediaType = "application/json")],
            )
        ]
    )
    @GetMapping("/{codigo}")
    @PreAuthorize("hasRole('ADMIN')")
    fun findByCodigo(@PathVariable codigo: String): ResponseEntity<VagaResponseDto> {
        val vaga = vagaService.findByCodigo(codigo)
        return ResponseEntity.ok(vaga.toVagaResponseDto())
    }

}