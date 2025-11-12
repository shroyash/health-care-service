package com.example.healthcare.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = "file:/app/uploads/profile/";

        log.info("=== Configuring Static Resource Handler ===");
        log.info("Mapping /uploads/documents/** to {}", uploadPath);

        registry.addResourceHandler("/uploads/profile/**")
                .addResourceLocations(uploadPath);

        log.info("✅ Resource handler configured successfully");
    }
}