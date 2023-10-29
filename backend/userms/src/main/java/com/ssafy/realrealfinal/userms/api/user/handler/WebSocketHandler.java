package com.ssafy.realrealfinal.userms.api.user.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.ssafy.realrealfinal.userms.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketHandler {

    private final SocketIOServer server;
    private final String USED_PIXEL = "usedPixel";
    private final UserService userService;

    //연결된 클라이언트의 Websocket 세션을 저장
    private static final ConcurrentHashMap<UUID, SocketIOClient> CLIENTS = new ConcurrentHashMap<>();

    @EventListener(ContextRefreshedEvent.class)
    public void start() {
        server.start();
    }

    // 클라이언트가 연결될 때 실행되는 메서드
    // TODO: 한명씩만 연결되어야 함
    @OnConnect
    public void onConnect(SocketIOClient client) {
        log.info("New client connected: {}", client.getSessionId().toString());
        CLIENTS.put(client.getSessionId(), client);
    }

    // 클라이언트에서 "used_pixel" 이벤트가 발생했을 때 실행되는 메서드
//    @OnEvent(USED_PIXEL)
//    public void onUsedPixelUpdateEvent(SocketIOClient client) {
//        log.info(USED_PIXEL+ " event received: {}");
//        if (CLIENTS == null || CLIENTS.size() == 0) {
//            return;
//        }
//        for (SocketIOClient clientSession : CLIENTS.values()) {
//            if (clientSession.isChannelOpen()) {
//                clientSession.sendEvent(USED_PIXEL);
//            }
//        }
//    }

    // TODO: 픽셀을 찍은 사용자만 받아야 함
    public void sendPixelCountToClients(Integer pixelCount) {
        for (SocketIOClient client : CLIENTS.values()) {
            if (client.isChannelOpen()) {
                client.sendEvent(USED_PIXEL, pixelCount);
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
