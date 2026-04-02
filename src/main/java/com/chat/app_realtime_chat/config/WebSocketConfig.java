package com.chat.app_realtime_chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat-websocket")
                // Use Patterns to safely allow your local React app
                .setAllowedOriginPatterns("http://localhost:5173", "https://localhost:5173")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // This tells Spring where to route messages coming FROM React
        registry.setApplicationDestinationPrefixes("/app");

        // THIS IS THE CRITICAL LINE: It tells Spring where it is allowed to push messages TO React
        registry.enableSimpleBroker("/topic");
    }
}
