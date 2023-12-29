package org.hygorm10.demoparkapi

import org.assertj.core.api.Assertions.assertThat
import org.hygorm10.demoparkapi.controller.dto.ClienteCreateDto
import org.hygorm10.demoparkapi.controller.dto.ClienteResponseDto
import org.hygorm10.demoparkapi.exception.ErrorMessage
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = ["/sql/clientes/clientes-insert.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = ["/sql/clientes/clientes-delete.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ClienteIT {

    @Autowired
    lateinit var testCliet: WebTestClient

    @Test
    fun `should create client success`() {
        val responseBody: ClienteResponseDto? = testCliet.post()
            .uri("/api/v1/clientes")
            .headers(JwtAuthentication.getHeaderAuthorization(testCliet, "toby@email.com", "123456"))
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(ClienteCreateDto(nome = "Tobias Marvado", cpf = "15244582011"))
            .exchange()
            .expectStatus().isCreated
            .expectBody(ClienteResponseDto::class.java)
            .returnResult().responseBody

        assertThat(responseBody).isNotNull
        assertThat(responseBody?.id).isNotNull
        assertThat(responseBody?.nome).isEqualTo("Tobias Marvado")
        assertThat(responseBody?.cpf).isEqualTo("15244582011")
    }

    @Test
    fun `should return 403 in create client success when user is admin`() {
        testCliet.post()
            .uri("/api/v1/clientes")
            .headers(JwtAuthentication.getHeaderAuthorization(testCliet, "ana@email.com", "123456"))
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(ClienteCreateDto(nome = "Tobias Marvado", cpf = "15244582011"))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    fun `should return 409 in create client success when cpf already insert`() {
        val responseBody: ErrorMessage? = testCliet.post()
            .uri("/api/v1/clientes")
            .headers(JwtAuthentication.getHeaderAuthorization(testCliet, "toby@email.com", "123456"))
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(ClienteCreateDto(nome = "Tobias Marvado", cpf = "56196897004"))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.CONFLICT)
            .expectBody(ErrorMessage::class.java)
            .returnResult().responseBody

        assertThat(responseBody).isNotNull
        assertThat(responseBody?.status).isEqualTo(HttpStatus.CONFLICT.value())
    }

    @Test
    fun `should find by id success`() {
        val responseBody: ClienteResponseDto? = testCliet.get()
            .uri("/api/v1/clientes/{id}", 10)
            .headers(JwtAuthentication.getHeaderAuthorization(testCliet, "ana@email.com", "123456"))
            .exchange()
            .expectStatus().isOk
            .expectBody(ClienteResponseDto::class.java)
            .returnResult().responseBody

        assertThat(responseBody).isNotNull
        assertThat(responseBody?.id).isNotNull
        assertThat(responseBody?.nome).isEqualTo("Bianca Silva")
        assertThat(responseBody?.cpf).isEqualTo("56196897004")
    }

    @Test
    fun `should return 404 in find by id when id not found`() {
        val responseBody: ErrorMessage? = testCliet.get()
            .uri("/api/v1/clientes/{id}", 100)
            .headers(JwtAuthentication.getHeaderAuthorization(testCliet, "ana@email.com", "123456"))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
            .expectBody(ErrorMessage::class.java)
            .returnResult().responseBody

        assertThat(responseBody).isNotNull
        assertThat(responseBody?.status).isEqualTo(HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun `should return 403 in find by id when user is role client`() {
        testCliet.get()
            .uri("/api/v1/clientes/{id}", 100)
            .headers(JwtAuthentication.getHeaderAuthorization(testCliet, "toby@email.com", "123456"))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.FORBIDDEN)
    }

}