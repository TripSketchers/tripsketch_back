package com.sketchers.tripsketch_back.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")      // 요청 엔드포인트
                .allowedMethods("*")    // 요청 메소드 허용
                .allowedOrigins("*");   // 요청 서버 허용
    }
}
