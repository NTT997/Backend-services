package com.salesmanager.core.business.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class LogWebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
    	System.out.println("AFTER SOCKET CONNECTION ESTABLISH");
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
    	System.out.println("AFTER SOCKET CONNECTION CLOSED");
    	
        sessions.remove(session);
    }

    public void sendLogToClients(String message) {
        sessions.forEach(session -> {
            try {
            	System.out.println("START SENDING TO CLIENT");
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
