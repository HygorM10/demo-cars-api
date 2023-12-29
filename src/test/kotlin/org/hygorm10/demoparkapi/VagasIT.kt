package org.hygorm10.demoparkapi

import org.assertj.core.api.Assertions.assertThat
import org.hygorm10.demoparkapi.controller.dto.VagaCreateDto
import org.hygorm10.demoparkapi.controller.dto.VagaResponseDto
import org.hygorm10.demoparkapi.exception.ErrorMessage
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = ["/sql/vagas/vagas-insert.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = ["/sql/vagas/vagas-delete.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class VagasIT {

    @Autowired
    lateinit var testCliet: WebTestClient

    @Test
    fun `should create vagas success`() {
        testCliet.post()
            .uri("/api/v1/vagas")
            .headers(JwtAuthentication.getHeaderAuthorization(testCliet, "ana@email.com", "123456"))
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(VagaCreateDto("A-05", "LIVRE"))
            .exchange()
            .expectStatus().isCreated
            .expectHeader().exists(HttpHeaders.LOCATION)
    }

    @Test
    fun `should return 409 in create vagas case codigo already`() {
        val responseBody: ErrorMessage? = testCliet.post()
            .uri("/api/v1/vagas")
            .headers(JwtAuthentication.getHeaderAuthorization(testCliet, "ana@email.com", "123456"))
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(VagaCreateDto("A-02", "LIVRE"))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.CONFLICT)
            .expectBody(ErrorMessage::class.java)
            .returnResult().responseBody

        assertThat(responseBody).isNotNull
        assertThat(responseBody?.status).isEqualTo(HttpStatus.CONFLICT.value())
    }

    @Test
    fun `should return 403 in create vagas case user not role ADMIN already`() {
        val responseBody: ErrorMessage? = testCliet.post()
            .uri("/api/v1/vagas")
            .headers(JwtAuthentication.getHeaderAuthorization(testCliet, "bob@email.com", "123456"))
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(VagaCreateDto("A-02", "LIVRE"))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.FORBIDDEN)
            .expectBody(ErrorMessage::class.java)
            .returnResult().responseBody

        assertThat(responseBody).isNotNull
        assertThat(responseBody?.status).isEqualTo(HttpStatus.FORBIDDEN.value())
    }

    @Test
    fun `should find vagas by codigo success`() {
        val responseBody: VagaResponseDto? = testCliet.get()
            .uri("/api/v1/vagas/{codigo}", "A-01")
            .headers(JwtAuthentication.getHeaderAuthorization(testCliet, "ana@email.com", "123456"))
            .exchange()
            .expectStatus().isOk
            .expectBody(VagaResponseDto::class.java)
            .returnResult().responseBody

        assertThat(responseBody).isNotNull
        assertThat(responseBody?.codigo).isEqualTo("A-01")
    }

    @Test
    fun `should return 404 in find vagas by codigo case not found codigo`() {
        val responseBody: ErrorMessage? = testCliet.get()
            .uri("/api/v1/vagas/{codigo}", "A-10")
            .headers(JwtAuthentication.getHeaderAuthorization(testCliet, "ana@email.com", "123456"))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
            .expectBody(ErrorMessage::class.java)
            .returnResult().responseBody

        assertThat(responseBody).isNotNull
        assertThat(responseBody?.status).isEqualTo(HttpStatus.NOT_FOUND.value())
    }

}