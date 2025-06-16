package vn.fu_ohayo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    //web socket cung cấp cho client kết nối đến server va trao đổi dữ liệu theo thời gian thực
    //web socket không cần sử dụng liên tục http request
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/notifications").setAllowedOrigins("*").withSockJS();
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(org.springframework.messaging.simp.config.MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }
}
