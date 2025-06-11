package com.sharediary.socket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharediary.auth.jwt.JwtProvider;
import com.sharediary.socket.delegate.DiaryEditDelegate;
import com.sharediary.socket.dto.DiaryEditMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiaryWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, List<WebSocketSession>> sessionMap = new ConcurrentHashMap<>();
    private final JwtProvider jwtProvider;
    private DiaryEditDelegate delegate;

    public void setDelegate(DiaryEditDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String diaryId = getQueryParam(session, "diaryId");
        String token = getQueryParam(session, "token");

        if (token == null || !jwtProvider.validateToken(token)) {
            closeWithError(session, "인증 실패: 유효하지 않은 토큰입니다.");
            return;
        }

        String userId = jwtProvider.getUserId(token);
        session.getAttributes().put("userId", userId);

        log.info("🧩 WebSocket 연결 요청 - userId: {}, diaryId: {}", userId, diaryId);

        if (!delegate.hasEditPermission(diaryId, userId)) {
            closeWithError(session, "접근 권한이 없습니다.");
            return;
        }

        sessionMap.computeIfAbsent(diaryId, k -> new ArrayList<>()).add(session);
        log.info("✅ WebSocket 연결됨 - diaryId={}, userId={}", diaryId, userId);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        DiaryEditMessageDto msg = objectMapper.readValue(message.getPayload(), DiaryEditMessageDto.class);
        String diaryId = msg.getDiaryId();
        String userId = (String) session.getAttributes().get("userId");

        msg.setUserId(userId); // 보안: 서버에서 덮어씀

        switch (msg.getType()) {
            case "EDIT" -> {
                delegate.applyEdit(diaryId, userId, msg.getContent(), msg.getTitle());
                broadcast(diaryId, objectMapper.writeValueAsString(msg)); // 보낸 사람 포함 전체 broadcast
            }
            case "TAG_ADD" -> {
                String taggedUserId = msg.getTaggedUserId();
                if (delegate.canTagFriend(userId, taggedUserId)) {
                    delegate.addTag(diaryId, taggedUserId);
                    broadcast(diaryId, objectMapper.writeValueAsString(msg)); // 모든 사람에게 전송
                } else {
                    session.sendMessage(new TextMessage("{\"error\":\"친구가 아니라 태그할 수 없습니다.\"}"));
                }
            }
            case "TAG_REMOVE" -> {
                String untaggedUserId = msg.getTaggedUserId();
                delegate.removeTag(diaryId, untaggedUserId);
                disconnectUserFromDiary(diaryId, untaggedUserId);
                broadcast(diaryId, objectMapper.writeValueAsString(msg));
            }
            default -> {
                log.warn("⚠️ 알 수 없는 메시지 타입: {}", msg.getType());
                session.sendMessage(new TextMessage("{\"error\":\"알 수 없는 메시지 타입입니다.\"}"));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionMap.values().forEach(list -> list.remove(session));
    }

    public void disconnectUserFromDiary(String diaryId, String userId) {
        List<WebSocketSession> sessions = sessionMap.getOrDefault(diaryId, List.of());
        sessions.stream()
                .filter(s -> userId.equals(s.getAttributes().get("userId")))
                .forEach(s -> {
                    try {
                        s.sendMessage(new TextMessage("{\"error\":\"태그가 삭제되어 연결이 종료됩니다.\"}"));
                        s.close(CloseStatus.NORMAL);
                    } catch (Exception e) {
                        log.warn("WebSocket 종료 실패: {}", e.getMessage());
                    }
                });
    }

    private void broadcast(String diaryId, String payload) {
        for (WebSocketSession s : sessionMap.getOrDefault(diaryId, List.of())) {
            if (s.isOpen()) {
                try {
                    s.sendMessage(new TextMessage(payload));
                } catch (Exception e) {
                    log.warn("브로드캐스트 실패: {}", e.getMessage());
                }
            }
        }
    }

    private void closeWithError(WebSocketSession session, String errorMessage) {
        try {
            session.sendMessage(new TextMessage("{\"error\":\"" + errorMessage + "\"}"));
            session.close(CloseStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            log.warn("WebSocket 강제 종료 실패: {}", e.getMessage());
        }
    }

    private String getQueryParam(WebSocketSession session, String key) {
        String query = Objects.requireNonNull(session.getUri()).getQuery();
        if (query == null) return null;
        for (String pair : query.split("&")) {
            String[] kv = pair.split("=");
            if (kv.length == 2 && kv[0].equals(key)) return kv[1];
        }
        return null;
    }
}
