package com.sketchers.tripsketch_back.config;

import com.sketchers.tripsketch_back.filter.JwtAuthenticationFilter;
import com.sketchers.tripsketch_back.security.PrincipalEntryPoint;
import com.sketchers.tripsketch_back.security.PrincipalUserDetailsService;
import com.sketchers.tripsketch_back.security.oauth2.OAuth2FailureHandler;
import com.sketchers.tripsketch_back.security.oauth2.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final PrincipalEntryPoint principalEntryPoint;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final PrincipalUserDetailsService principalUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors()
            .and()
            .csrf().disable()
            .authorizeHttpRequests()
            .antMatchers("/api/auth/**", "/api/account/auth/email")
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
            .authenticationEntryPoint(principalEntryPoint)
            .and()
            .oauth2Login()
            .successHandler(oAuth2SuccessHandler)
            .failureHandler(oAuth2FailureHandler)
            .userInfoEndpoint()
            .userService(principalUserDetailsService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
