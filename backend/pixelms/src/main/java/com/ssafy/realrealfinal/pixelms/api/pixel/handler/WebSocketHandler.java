package com.ssafy.realrealfinal.pixelms.api.pixel.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.ssafy.realrealfinal.pixelms.api.pixel.dto.PixelDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketHandler {

    private final SocketIOServer server;


    //연결된 클라이언트의 Websocket 세션을 저장
    private static final ConcurrentHashMap<UUID, SocketIOClient> CLIENTS = new ConcurrentHashMap<>();

    @EventListener(ContextRefreshedEvent.class)
    public void start() {
        server.start();
    }

    // 클라이언트가 연결될 때 실행되는 메서드
    @OnConnect
    public void onConnect(SocketIOClient client) {
        log.info("New client connected: {}", client.getSessionId().toString());
        CLIENTS.put(client.getSessionId(),client);
    }

    // "pixel" 이벤트가 발생했을 때 실행되는 메서드
    @OnEvent("pixel")
    public void onPixelEvent(SocketIOClient client, List<PixelDTO> pixelDtoList) {
        log.info("Pixel event received: {}", pixelDtoList);
//        server.getBroadcastOperations().sendEvent("pixel", pixelDto);
        for (SocketIOClient clientSession : CLIENTS.values()){
            if(clientSession.isChannelOpen()){
                clientSession.sendEvent("pixel",pixelDtoList);
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