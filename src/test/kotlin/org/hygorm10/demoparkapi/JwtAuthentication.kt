package org.hygorm10.demoparkapi

import org.hygorm10.demoparkapi.config.jwt.JwtToken
import org.hygorm10.demoparkapi.controller.dto.UsuarioLoginDto
import org.springframework.http.HttpHeaders
import org.springframework.test.web.reactive.server.WebTestClient
import java.util.function.Consumer

class JwtAuthentication {

    companion object {
        fun getHeaderAuthorization(client: WebTestClient, username: String, password: String): Consumer<HttpHeaders> {
            val token = client.post()
                .uri("/api/v1/auth")
                .bodyValue(UsuarioLoginDto(username = username, password = password))
                .exchange()
                .expectStatus().isOk
                .expectBody(JwtToken::class.java)
                .returnResult().responseBody?.token

            return Consumer { headers: HttpHeaders -> headers.setBearerAuth(token!!) }
        }
    }

}