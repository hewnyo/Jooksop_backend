package com.sharediary.socket.interceptor;

import com.sharediary.auth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.Map;

@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {

        URI uri = request.getURI();
        String query = uri.getQuery();
        if (query == null || !query.contains("token=")) {
            System.out.println("🔴 JwtHandshakeInterceptor - query 없음 또는 토큰 누락");
            return false;
        }

        String token = extractQueryParam(query, "token");
        if (token == null || !jwtProvider.validateToken(token)) {
            System.out.println("🔴 JwtHandshakeInterceptor - 유효하지 않은 토큰: " + token);
            return false;
        }

        String userId = jwtProvider.getUserId(token);
        System.out.println("🟢 JwtHandshakeInterceptor - userId from token: " + userId);
        attributes.put("userId", userId);

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // 필요 시 로깅 가능
    }

    private String extractQueryParam(String query, String key) {
        for (String pair : query.split("&")) {
            String[] kv = pair.split("=");
            if (kv.length == 2 && kv[0].equals(key)) {
                return kv[1];
            }
        }
        return null;
    }
}
