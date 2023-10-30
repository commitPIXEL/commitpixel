package com.ssafy.realrealfinal.pixelms.api.pixel.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.ssafy.realrealfinal.pixelms.api.pixel.dto.PixelDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketHandler {

    private final SocketIOServer server;

    // 클라이언트의 providerId가 key, 연결된 클라이언트의 Websocket 세션이 value
    private static final ConcurrentHashMap<UUID, Map<Integer, SocketIOClient>> CLIENTS = new ConcurrentHashMap<>();

    @EventListener(ContextRefreshedEvent.class)
    public void start() {
        server.start();
    }

    // 클라이언트가 최초로 연결될 때 실행되는 메서드
    @OnConnect
    public void onConnect(SocketIOClient client) {
        log.info("New client connected: {}", client.getSessionId().toString());

        Map<Integer, SocketIOClient> socketMap = new ConcurrentHashMap<>();
        String accessToken = client.getHandshakeData().getHttpHeaders().get("Authorization");
        if (accessToken == null || accessToken.isEmpty() || accessToken.equals("")) {
            socketMap.put(-1, client);
        } else {
            Integer providerId = 1; // TODO: feign으로 auth와 연결
            socketMap.put(providerId, client);
        }
        CLIENTS.put(client.getSessionId(), socketMap);
    }

    // "pixel" 이벤트가 발생했을 때 실행되는 메서드
    @OnEvent("pixel")
    public void onPixelEvent(SocketIOClient client, List<PixelDto> pixelDtoList) {
        log.info("Pixel event received: {}", pixelDtoList);
        if (CLIENTS == null || CLIENTS.size() == 0) {
            return;
        }
        for (Map<Integer, SocketIOClient> clientMap : CLIENTS.values()) {
            for (SocketIOClient clientSession : clientMap.values()) {
                if (!client.getSessionId().equals(clientSession.getSessionId()) && clientSession.isChannelOpen()) {
                    clientSession.sendEvent("pixel", pixelDtoList);
                }
            }
        }
    }

    // 클라이언트가 연결을 끊을 때 실행되는 메서드
    @OnDisconnect
    public void onDisconnectEvent(SocketIOClient client) {
        log.info("Client disconnected: {}", client.getSessionId().toString());
        CLIENTS.remove(client.getSessionId());
    }
}