package org.hygorm10.demoparkapi

import org.hygorm10.demoparkapi.controller.dto.EstacionamentoClienteDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(
    scripts = ["/sql/estacionamentos/estacionamentos-insert.sql"],
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@Sql(
    scripts = ["/sql/estacionamentos/estacionamentos-delete.sql"],
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)
class EstacionamentoIT {

    @Autowired
    lateinit var testCliet: WebTestClient

    @Test
    fun `should check-in estacionamento success`() {
        val estacionamentoCreateDto = EstacionamentoClienteDto(
            placa = "ABC-1234",
            marca = "HONDA",
            modelo = "CIVIC",
            cor = "PRETO",
            clienteCpf = "56196897004"
        )
        testCliet.post()
            .uri("/api/v1/estacionamentos/check-in")
            .headers(JwtAuthentication.getHeaderAuthorization(testCliet, "ana@email.com", "123456"))
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(estacionamentoCreateDto)
            .exchange()
            .expectStatus().isCreated
            .expectHeader().exists(HttpHeaders.LOCATION)
            .expectBody()
            .jsonPath("placa").isEqualTo("ABC-1234")
            .jsonPath("marca").isEqualTo("HONDA")
            .jsonPath("modelo").isEqualTo("CIVIC")
            .jsonPath("cor").isEqualTo("PRETO")
            .jsonPath("clienteCpf").isEqualTo("56196897004")
            .jsonPath("vagaCodigo").isEqualTo("A-02")
            .jsonPath("dataEntrada").exists()
    }

    @Test
    fun `should return 403 in check-in estacionamento when user dont admin`() {
        val estacionamentoCreateDto = EstacionamentoClienteDto(
            placa = "ABC-1234",
            marca = "HONDA",
            modelo = "CIVIC",
            cor = "PRETO",
            clienteCpf = "56196897004"
        )
        testCliet.post()
            .uri("/api/v1/estacionamentos/check-in")
            .headers(JwtAuthentication.getHeaderAuthorization(testCliet, "bia@email.com", "123456"))
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(estacionamentoCreateDto)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.FORBIDDEN)
            .expectBody()
            .jsonPath("status").isEqualTo(HttpStatus.FORBIDDEN.value())
            .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-in")
            .jsonPath("method").isEqualTo("POST")
    }

    @Test
    fun `should return 422 in check-in estacionamento when dto is invalid`() {
        val estacionamentoCreateDto = EstacionamentoClienteDto(
            placa = "ABC1234",
            marca = "HONDA",
            modelo = "CIVIC",
            cor = "PRETO",
            clienteCpf = "56196897004"
        )
        testCliet.post()
            .uri("/api/v1/estacionamentos/check-in")
            .headers(JwtAuthentication.getHeaderAuthorization(testCliet, "ana@email.com", "123456"))
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(estacionamentoCreateDto)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
            .expectBody()
            .jsonPath("status").isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value())
            .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-in")
            .jsonPath("method").isEqualTo("POST")
    }

    @Test
    fun `should return 404 in check-in estacionamento when cpf is invalid`() {
        val estacionamentoCreateDto = EstacionamentoClienteDto(
            placa = "ABC-1234",
            marca = "HONDA",
            modelo = "CIVIC",
            cor = "PRETO",
            clienteCpf = "15760048058"
        )
        testCliet.post()
            .uri("/api/v1/estacionamentos/check-in")
            .headers(JwtAuthentication.getHeaderAuthorization(testCliet, "ana@email.com", "123456"))
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(estacionamentoCreateDto)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
            .expectBody()
            .jsonPath("status").isEqualTo(HttpStatus.NOT_FOUND.value())
            .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-in")
            .jsonPath("method").isEqualTo("POST")
    }

    @Sql(
        scripts = ["/sql/estacionamentos/estacionamentos-insert-vagas-ocupadas.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
        scripts = ["/sql/estacionamentos/estacionamentos-delete.sql"],
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    fun `should return 404 in check-in estacionamento when all vagas is busy`() {
        val estacionamentoCreateDto = EstacionamentoClienteDto(
            placa = "ABC-1234",
            marca = "HONDA",
            modelo = "CIVIC",
            cor = "PRETO",
            clienteCpf = "56196897004"
        )
        testCliet.post()
            .uri("/api/v1/estacionamentos/check-in")
            .headers(JwtAuthentication.getHeaderAuthorization(testCliet, "ana@email.com", "123456"))
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(estacionamentoCreateDto)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
            .expectBody()
            .jsonPath("status").isEqualTo(HttpStatus.NOT_FOUND.value())
            .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-in")
            .jsonPath("method").isEqualTo("POST")
    }

    @Test
    fun `should find check-in estacionamento success`() {
        testCliet.get()
            .uri("/api/v1/estacionamentos/check-in/{recibo}", "20230313-101300")
            .headers(JwtAuthentication.getHeaderAuthorization(testCliet, "ana@email.com", "123456"))
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("placa").isEqualTo("FIT-1020")
            .jsonPath("marca").isEqualTo("FIAT")
            .jsonPath("modelo").isEqualTo("PALIO")
            .jsonPath("cor").isEqualTo("VERDE")
            .jsonPath("clienteCpf").isEqualTo("56196897004")
            .jsonPath("vagaCodigo").isEqualTo("A-01")
            .jsonPath("dataEntrada").exists()
    }

    @Test
    fun `should check-out estacionamento success`() {
        testCliet.put()
            .uri("/api/v1/estacionamentos/check-out/{recibo}", "20230313-101300")
            .headers(JwtAuthentication.getHeaderAuthorization(testCliet, "ana@email.com", "123456"))
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("placa").isEqualTo("FIT-1020")
            .jsonPath("marca").isEqualTo("FIAT")
            .jsonPath("modelo").isEqualTo("PALIO")
            .jsonPath("cor").isEqualTo("VERDE")
            .jsonPath("clienteCpf").isEqualTo("56196897004")
            .jsonPath("vagaCodigo").isEqualTo("A-01")
            .jsonPath("dataEntrada").exists()
            .jsonPath("dataSaida").exists()
            .jsonPath("valor").exists()
            .jsonPath("desconto").exists()
    }

}