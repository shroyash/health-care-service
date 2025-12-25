package com.example.healthcare.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Get the project root directory
        String projectRoot = System.getProperty("user.dir");
        String uploadPath = "file:" + projectRoot + "/uploads/";

        log.info("=== Configuring Static Resource Handler ===");
        log.info("Project Root: {}", projectRoot);
        log.info("Upload Path: {}", uploadPath);
        log.info("URL Pattern: /uploads/**");
        log.info("This will serve files from: {}", projectRoot + "/uploads/");

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath)
                .setCachePeriod(0); // Disable cache during development

        log.info("✅ Resource handler configured successfully");
        log.info("Example URL: http://localhost:8004/uploads/profile/profile/1765898201266_shroyash.jpeg");
    }

}