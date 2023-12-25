package org.hygorm10.demoparkapi.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing


@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@Configuration
class JpaConfig {

    @Bean
    fun auditorProvider(): AuditorAware<String> {
        return SpringJpaAuditingConfig()
    }

}