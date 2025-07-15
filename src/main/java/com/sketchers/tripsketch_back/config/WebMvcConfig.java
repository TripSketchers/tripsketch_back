package com.sketchers.tripsketch_back.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://tripsketchers.github.io/tripsketch_front/") // 프론트 배포 주소
                .allowedMethods("*")
                .allowedHeaders("*");
    }
}
