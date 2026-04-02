package com.chat.app_realtime_chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Disable CSRF so SockJS can establish the connection
                .csrf(csrf -> csrf.disable())

                // 2. Disable Frame Options so SockJS iframes don't get blocked
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))

                // ... your existing authorizeHttpRequests and oauth2Login rules go here ...
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/oauth2/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("http://localhost:5173", true)
                );

        return http.build();
    }
}