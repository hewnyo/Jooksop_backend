package com.sharediary.socket.config;

import com.sharediary.auth.jwt.JwtProvider;
import com.sharediary.socket.handler.DiaryWebSocketHandler;
import com.sharediary.socket.interceptor.JwtHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final DiaryWebSocketHandler diaryWebSocketHandler;
    private final JwtProvider jwtProvider;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(diaryWebSocketHandler, "/ws/diary")
                .addInterceptors(new JwtHandshakeInterceptor(jwtProvider))
                .setAllowedOrigins("*");
    }
}
