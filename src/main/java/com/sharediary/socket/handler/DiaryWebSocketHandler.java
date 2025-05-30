package com.sharediary.socket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharediary.diary.service.DiaryService;
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
    private DiaryEditDelegate delegate;


    public void setDelegate(DiaryEditDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String diaryId = getQueryParam(session, "diaryId");
        String userId = (String) session.getAttributes().get("userId");

        // ✅ 로그 추가
        System.out.println("🧩 WebSocket 연결 요청 - userId: " + userId + ", diaryId: " + diaryId);

        if (!delegate.hasEditPermission(diaryId, userId)) {
            try {
                session.sendMessage(new TextMessage("{\"error\":\"접근 권한이 없습니다.\"}"));
                session.close(CloseStatus.NOT_ACCEPTABLE);
            } catch (Exception e) {
                log.warn("WebSocket 강제 종료 실패: {}", e.getMessage());
            }
            return;
        }

        sessionMap.computeIfAbsent(diaryId, k -> new ArrayList<>()).add(session);
        log.info("WebSocket 연결됨 - diaryId={}, userId={}", diaryId, userId);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String diaryId = getQueryParam(session, "diaryId");
        String userId = (String) session.getAttributes().get("userId");

        DiaryEditMessageDto msg = objectMapper.readValue(message.getPayload(), DiaryEditMessageDto.class);
        msg.setDiaryId(diaryId);

        delegate.applyEdit(diaryId, userId, msg.getContent());

        for (WebSocketSession s : sessionMap.getOrDefault(diaryId, List.of())) {
            if (s.isOpen() && !s.getId().equals(session.getId())) {
                s.sendMessage(new TextMessage(objectMapper.writeValueAsString(msg)));
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
