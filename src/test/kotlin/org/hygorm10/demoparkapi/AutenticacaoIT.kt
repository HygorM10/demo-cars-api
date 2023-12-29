package org.hygorm10.demoparkapi

import org.assertj.core.api.Assertions.assertThat
import org.hygorm10.demoparkapi.config.jwt.JwtToken
import org.hygorm10.demoparkapi.controller.dto.UsuarioLoginDto
import org.hygorm10.demoparkapi.exception.ErrorMessage
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = ["/sql/usuarios/usuarios-insert.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = ["/sql/usuarios/usuarios-delete.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AutenticacaoIT {

    @Autowired
    lateinit var testCliet: WebTestClient

    @Test
    fun `should authentication user at api with success`() {
        val responseBody = testCliet.post()
            .uri("/api/v1/auth")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(UsuarioLoginDto(username = "ana@email.com", password = "123456"))
            .exchange()
            .expectStatus().isOk
            .expectBody(JwtToken::class.java)
            .returnResult().responseBody

        assertThat(responseBody).isNotNull
    }

    @Test
    fun `should return status code 400 in authentication user at api with username or password invalid`() {
        var responseBody = testCliet.post()
            .uri("/api/v1/auth")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(UsuarioLoginDto(username = "invalid@email.com", password = "123456"))
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(ErrorMessage::class.java)
            .returnResult().responseBody

        assertThat(responseBody).isNotNull
        assertThat(responseBody?.status).isEqualTo(HttpStatus.BAD_REQUEST.value())

        responseBody = testCliet.post()
            .uri("/api/v1/auth")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(UsuarioLoginDto(username = "ana@email.com", password = "123457"))
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(ErrorMessage::class.java)
            .returnResult().responseBody

        assertThat(responseBody).isNotNull
        assertThat(responseBody?.status).isEqualTo(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    fun `should send status 422 in authentication user at api with username or password format invalid`() {
        var responseBody = testCliet.post()
            .uri("/api/v1/auth")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(UsuarioLoginDto(username = "invalid.email.com", password = "123456"))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
            .expectBody(ErrorMessage::class.java)
            .returnResult().responseBody

        assertThat(responseBody).isNotNull
        assertThat(responseBody?.status).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value())

        responseBody = testCliet.post()
            .uri("/api/v1/auth")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(UsuarioLoginDto(username = "ana@email.com", password = ""))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
            .expectBody(ErrorMessage::class.java)
            .returnResult().responseBody

        assertThat(responseBody).isNotNull
        assertThat(responseBody?.status).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value())
    }

}