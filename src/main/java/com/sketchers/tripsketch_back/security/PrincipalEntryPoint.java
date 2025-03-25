package com.sketchers.tripsketch_back.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class PrincipalEntryPoint implements AuthenticationEntryPoint {
    // 인증 실패 예외 처리 EntryPoint
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());   // 접근 금지 상태
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("unauthorized", "인증 실패");
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().println(objectMapper.writeValueAsString(errorMap));    // Map을 json으로 바꿔서 응답
    }
}
