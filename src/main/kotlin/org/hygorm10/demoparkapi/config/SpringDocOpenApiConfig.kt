package org.hygorm10.demoparkapi.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SpringDocOpenApiConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("REST API - Spring Park")
                    .description("API para gestão de estacionamento de veículos")
                    .version("1.0.0")
                    .license(License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))
                    .contact(Contact().name("Hygor Martins").email("hygor.martins74@live.com"))
            )
    }

}