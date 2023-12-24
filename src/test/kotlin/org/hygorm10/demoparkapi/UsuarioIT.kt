package org.hygorm10.demoparkapi

import org.assertj.core.api.Assertions.assertThat
import org.hygorm10.demoparkapi.controller.dto.UsuarioCreateDto
import org.hygorm10.demoparkapi.controller.dto.UsuarioPasswordUpdateDto
import org.hygorm10.demoparkapi.controller.dto.UsuarioResponseDto
import org.hygorm10.demoparkapi.exception.ErrorMessage
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = ["/sql/usuarios/usuarios-insert.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = ["/sql/usuarios/usuarios-delete.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UsuarioIT {

    @Autowired
    lateinit var testCliet: WebTestClient

    @Test
    fun `should create user success`() {
        val responseBody: UsuarioResponseDto? = testCliet.post()
            .uri("/api/v1/usuarios")
            .contentType(APPLICATION_JSON)
            .bodyValue(UsuarioCreateDto(username = "hygor@gmail.com", password = "123456"))
            .exchange()
            .expectStatus().isCreated
            .expectBody(UsuarioResponseDto::class.java)
            .returnResult().responseBody

        assertThat(responseBody).isNotNull
        assertThat(responseBody?.id).isNotNull
        assertThat(responseBody?.username).isEqualTo("hygor@gmail.com")
        assertThat(responseBody?.role).isEqualTo("CLIENTE")
    }

    @Test
    fun `should return status code 422 in create user with username incorrect`() {
        val responseBody: ErrorMessage? = testCliet.post()
            .uri("/api/v1/usuarios")
            .contentType(APPLICATION_JSON)
            .bodyValue(UsuarioCreateDto(username = "", password = "123456"))
            .exchange()
            .expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
            .expectBody(ErrorMessage::class.java)
            .returnResult().responseBody

        assertThat(responseBody).isNotNull
        assertThat(responseBody?.status).isEqualTo(422)
    }

    @Test
    fun `should return status code 422 in create user with password incorrect`() {
        val responseBody: ErrorMessage? = testCliet.post()
            .uri("/api/v1/usuarios")
            .contentType(APPLICATION_JSON)
            .bodyValue(UsuarioCreateDto(username = "hygor@gmail.com", password = ""))
            .exchange()
            .expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
            .expectBody(ErrorMessage::class.java)
            .returnResult().responseBody

        assertThat(responseBody).isNotNull
        assertThat(responseBody?.status).isEqualTo(422)
    }

    @Test
    fun `should return status code 409 in create user case username already exists`() {
        val responseBody: ErrorMessage? = testCliet.post()
            .uri("/api/v1/usuarios")
            .contentType(APPLICATION_JSON)
            .bodyValue(UsuarioCreateDto(username = "ana@email.com", password = "123456"))
            .exchange()
            .expectStatus().isEqualTo(CONFLICT)
            .expectBody(ErrorMessage::class.java)
            .returnResult().responseBody

        assertThat(responseBody).isNotNull
        assertThat(responseBody?.status).isEqualTo(409)
    }

    @Test
    fun `should find user by id success`() {
        val responseBody: UsuarioResponseDto? = testCliet.get()
            .uri("/api/v1/usuarios/{id}", 100)
            .exchange()
            .expectStatus().isOk
            .expectBody(UsuarioResponseDto::class.java)
            .returnResult().responseBody

        assertThat(responseBody).isNotNull
        assertThat(responseBody?.id).isEqualTo(100)
        assertThat(responseBody?.username).isEqualTo("ana@email.com")
        assertThat(responseBody?.role).isEqualTo("ADMIN")
    }

    @Test
    fun `should return status code 404 case user not found`() {
        val responseBody: ErrorMessage? = testCliet.get()
            .uri("/api/v1/usuarios/{id}", 0)
            .exchange()
            .expectStatus().isNotFound
            .expectBody(ErrorMessage::class.java)
            .returnResult().responseBody

        assertThat(responseBody).isNotNull
        assertThat(responseBody?.status).isEqualTo(404)
    }

    @Test
    fun `should update password by id with success`() {
        testCliet.patch()
            .uri("/api/v1/usuarios/{id}", 100)
            .contentType(APPLICATION_JSON)
            .bodyValue(UsuarioPasswordUpdateDto("123456", "123457", "123457"))
            .exchange()
            .expectStatus().isNoContent
    }

    @Test
    fun `should return status code 404 in update password by id case user not found`() {
        testCliet.patch()
            .uri("/api/v1/usuarios/{id}", 0)
            .contentType(APPLICATION_JSON)
            .bodyValue(UsuarioPasswordUpdateDto("123456", "123457", "123457"))
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `should return status code 400 in update password by id case old password is invalid`() {
        val responseBody = testCliet.patch()
            .uri("/api/v1/usuarios/{id}", 100)
            .contentType(APPLICATION_JSON)
            .bodyValue(UsuarioPasswordUpdateDto("123451", "123456", "123456"))
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(ErrorMessage::class.java)
            .returnResult().responseBody

        assertThat(responseBody).isNotNull
        assertThat(responseBody?.status).isEqualTo(400)
    }

    @Test
    fun `should return status code 400 in update password by id case new password and confirmation password is different`() {
        val responseBody = testCliet.patch()
            .uri("/api/v1/usuarios/{id}", 100)
            .contentType(APPLICATION_JSON)
            .bodyValue(UsuarioPasswordUpdateDto("123457", "123459", "123458"))
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(ErrorMessage::class.java)
            .returnResult().responseBody

        assertThat(responseBody).isNotNull
        assertThat(responseBody?.status).isEqualTo(400)
    }

    @Test
    fun `should return status code 422 in update password by id case password is invalid`() {
        val responseBody = testCliet.patch()
            .uri("/api/v1/usuarios/{id}", 100)
            .contentType(APPLICATION_JSON)
            .bodyValue(UsuarioPasswordUpdateDto("", "", ""))
            .exchange()
            .expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
            .expectBody(ErrorMessage::class.java)
            .returnResult().responseBody

        assertThat(responseBody).isNotNull
        assertThat(responseBody?.status).isEqualTo(422)
    }

    @Test
    fun `should list all users success`() {
        val responseBody: List<UsuarioResponseDto>? = testCliet.get()
            .uri("/api/v1/usuarios")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(UsuarioResponseDto::class.java)
            .returnResult().responseBody

        assertThat(responseBody).isNotNull
        assertThat(responseBody?.size).isEqualTo(3)
        assertThat(responseBody?.firstOrNull()?.username).isEqualTo("ana@email.com")
    }

}