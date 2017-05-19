package com.czechowski.webapp.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.czechowski.webapp.repositories.")
public class JpaConfig {
}
