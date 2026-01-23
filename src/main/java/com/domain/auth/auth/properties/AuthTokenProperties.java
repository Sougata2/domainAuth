package com.domain.auth.auth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "auth")
public class AuthTokenProperties {
    private String secret;
    private long accessExpiration;
    private long refreshExpiration;
}
