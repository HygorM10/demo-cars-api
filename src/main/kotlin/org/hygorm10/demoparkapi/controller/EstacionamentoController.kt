package org.hygorm10.demoparkapi.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.hygorm10.demoparkapi.controller.dto.EstacionamentoClienteDto
import org.hygorm10.demoparkapi.controller.dto.EstacionamentoResponseDto
import org.hygorm10.demoparkapi.entity.ClienteVaga
import org.hygorm10.demoparkapi.service.ClienteVagaService
import org.hygorm10.demoparkapi.service.EstacionamentoService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@Tag(
    name = "Estacionamentos",
    description = "Operações de registros de entrada e saída de um veículo do estacionamento."
)
@RestController
@RequestMapping("api/v1/estacionamentos")
class EstacionamentoController(
    val estacionamentoService: EstacionamentoService,
    val clienteVagaService: ClienteVagaService
) {

    @Operation(
        summary = "Operação de check-in",
        description = "Recurso para dar entrada de um veiculo no estacionamento.",
        security = [SecurityRequirement(name = "security")],
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "Recurso criado com sucesso.",
                content = [Content(mediaType = "application/json")],
            ),
            ApiResponse(
                responseCode = "404",
                description = "Cliente CPF não cadastrado no sistema.",
                content = [Content(mediaType = "application/json")],
            ),
            ApiResponse(
                responseCode = "422",
                description = "Recurso não processado por dados de entrada inválidos.",
                content = [Content(mediaType = "application/json")],
            )
        ]
    )
    @PostMapping("/check-in")
    @PreAuthorize("hasRole('ADMIN')")
    fun create(@RequestBody @Valid dto: EstacionamentoClienteDto): ResponseEntity<EstacionamentoResponseDto> {
        val clienteVaga: ClienteVaga = dto.toClienteVaga()
        val responseDto: EstacionamentoResponseDto = estacionamentoService.checkIn(clienteVaga)
            .toEstacionamentoResponseDto()
        val location: URI = ServletUriComponentsBuilder
            .fromCurrentRequestUri().path("/{recibo}")
            .buildAndExpand(clienteVaga.recibo)
            .toUri()
        return ResponseEntity.created(location).body(responseDto)
    }

    @Operation(
        summary = "Operação de consulta por recebi",
        description = "Recurso para dar consultar um estacionamento por recibo.",
        security = [SecurityRequirement(name = "security")],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Recurso encontrado com sucesso.",
                content = [Content(mediaType = "application/json")],
            ),
            ApiResponse(
                responseCode = "404",
                description = "Estacionamentio não encontrado no sistema.",
                content = [Content(mediaType = "application/json")],
            ),
            ApiResponse(
                responseCode = "403",
                description = "Usuario não atualizado no sistema.",
                content = [Content(mediaType = "application/json")],
            )
        ]
    )
    @GetMapping("/check-in/{recibo}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    fun findByRecibo(@PathVariable recibo: String): ResponseEntity<EstacionamentoResponseDto> {
        return ResponseEntity.ok(clienteVagaService.findByRecibo(recibo).toEstacionamentoResponseDto())
    }

    @PutMapping("/check-out/{recibo}")
    @PreAuthorize("hasRole('ADMIN')")
    fun checkOut(@PathVariable recibo: String): ResponseEntity<EstacionamentoResponseDto> {
        val clienteVaga = estacionamentoService.checkOut(recibo)
        val dto = clienteVaga.toEstacionamentoResponseDto()
        return ResponseEntity.ok(dto)
    }

}