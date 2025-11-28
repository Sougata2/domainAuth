package com.domain.auth.config.common;

import com.domain.mapper.mapping.Mapping;
import com.domain.mapper.service.MapperService;
import com.domain.mapper.service.impl.MapperServiceImpl;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {
    @Bean
    public MapperService mapperService(Mapping mapping, EntityManager entityManager) {
        return new MapperServiceImpl(mapping, entityManager);
    }
}
