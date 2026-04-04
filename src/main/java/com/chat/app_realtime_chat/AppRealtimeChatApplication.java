package com.chat.app_realtime_chat;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;

@SpringBootApplication
public class AppRealtimeChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppRealtimeChatApplication.class, args);
    }

    // THIS BEAN FORCES THE TOMCAT SERVER TO ACCEPT FILES UP TO 50MB
    // It overrides any conflicting settings injected by Azure's environment
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // Set the absolute maximum size of a single uploaded file
        factory.setMaxFileSize(DataSize.ofMegabytes(50));
        // Set the absolute maximum size of the entire HTTP POST request
        factory.setMaxRequestSize(DataSize.ofMegabytes(50));
        return factory.createMultipartConfig();
    }
}
