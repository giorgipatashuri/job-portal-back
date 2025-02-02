package by.giorgi.jobportalback.config;

import by.giorgi.jobportalback.model.dto.response.AuthResp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String email = extractEmail(session);
        sessions.put(email, session);
        System.out.println(email);
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String email = extractEmail(session);
        sessions.remove(email);
    }
    public void notifyEmailVerification(String email,String token) {
        WebSocketSession session = sessions.get(email);
        if (session != null && session.isOpen()) {
            try{
                AuthResp response = new AuthResp(token);
                System.out.println(response);
                session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(response)));
            }catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String extractEmail(WebSocketSession session) {
        String path = session.getUri().getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }
}
