package com.ssafy.realrealfinal.pixelms.api.pixel.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.ssafy.realrealfinal.pixelms.api.pixel.dto.SocketClientInfoDto;
import com.ssafy.realrealfinal.pixelms.api.pixel.feignClient.AuthFeignClient;
import com.ssafy.realrealfinal.pixelms.api.pixel.response.PixelInfoRes;
import com.ssafy.realrealfinal.pixelms.api.pixel.service.PixelService;
import com.ssafy.realrealfinal.pixelms.common.util.RedisUtil;
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

    private final PixelService pixelService;
    private final RedisUtil redisUtil;
    private final AuthFeignClient authFeignClient;
    private final SocketIOServer server;
    private final int SCALE = 1024;

    // 클라이언트의 providerId가 key, 연결된 클라이언트의 Websocket 세션이 value
    private static final ConcurrentHashMap<UUID, SocketClientInfoDto> CLIENTS = new ConcurrentHashMap<>();

    @EventListener(ContextRefreshedEvent.class)
    public void start() {
        server.start();
    }

    // 클라이언트가 최초로 연결될 때 실행되는 메서드
    @OnConnect
    public void onConnect(SocketIOClient client) {
        log.info("New client connected: {}", client.getSessionId().toString());

        String accessToken = client.getHandshakeData().getHttpHeaders().get("Authorization");
        if (accessToken == null || accessToken.isEmpty() || accessToken.equals("")) {
            CLIENTS.put(client.getSessionId(), new SocketClientInfoDto(-1, client));
        } else {
            Integer providerId = authFeignClient.withQueryString(accessToken);
            CLIENTS.put(client.getSessionId(), new SocketClientInfoDto(providerId, client));
        }

    }

    /**
     * "pixel" 이벤트가 발생했을 때 실행되는 메서드
     * 사용자가 픽셀 한 개를 찍음
     *
     * @param client
     * @param pixelDtoList 픽셀 한개의 정보 [x, y, r, g, b, url, userName]
     */
    @OnEvent("pixel")
    public void onPixelEvent(SocketIOClient client, List pixelDtoList) {
        log.info("Pixel event received: {}", pixelDtoList);
        // 사용자가 없을 때
        if (CLIENTS == null || CLIENTS.size() == 0) {
            return;
        }
        // (x * 1024 + y) 인덱스
        Integer index = (Integer) pixelDtoList.get(0) * SCALE + (Integer) pixelDtoList.get(1);
        // Red
        redisUtil.setData(String.valueOf(index), "R", (Integer) pixelDtoList.get(2));
        // Green
        redisUtil.setData(String.valueOf(index), "G", (Integer) pixelDtoList.get(3));
        // Blue
        redisUtil.setData(String.valueOf(index), "B", (Integer) pixelDtoList.get(4));
        // Url
        redisUtil.setData(String.valueOf(index), "url", (String) pixelDtoList.get(5));
        // UserId
        redisUtil.setData(String.valueOf(index), "id", (String) pixelDtoList.get(6));
        for (SocketClientInfoDto clientInfo : CLIENTS.values()) {
            SocketIOClient clientSession = clientInfo.getSocketIOClient();
            // 나를 제외한 모든 사용자에게 픽셀 변경 사항을 보내줌
            if (!client.getSessionId().equals(clientSession.getSessionId()) && clientSession.isChannelOpen()) {
                clientSession.sendEvent("pixel", pixelDtoList);
            }
        }

        Integer providerId = CLIENTS.get(client.getSessionId()).getProviderId();
        // 비회원이면 return
        if (providerId == -1) {
            return;
        }
        // 현재 클라이언트의 usedPixel redis 값 변경
        pixelService.updateUsedPixel(providerId);
        Integer availableCredit = pixelService.getAvailableCredit(providerId);
        client.sendEvent("pixel", availableCredit);
        return;
    }

    /**
     * 픽셀 클릭 시 해당 url과 작성자를 반환
     *
     * @param client
     * @param pixelDtoList 클릭한 픽셀의 위치 [x, y]
     */
    @OnEvent("url")
    public void onUrlEvent(SocketIOClient client, List pixelDtoList) {
        log.info("Url event received: {}", pixelDtoList);

        Integer index = (Integer) pixelDtoList.get(0) * SCALE + (Integer) pixelDtoList.get(1);
        String url = redisUtil.getStringData(String.valueOf(index), "url");
        String githubNickname = redisUtil.getStringData(String.valueOf(index), "id");
        PixelInfoRes pixelInfoRes = new PixelInfoRes(url, githubNickname);
        client.sendEvent("url", pixelInfoRes);
    }

    // 클라이언트가 연결을 끊을 때 실행되는 메서드
    @OnDisconnect
    public void onDisconnectEvent(SocketIOClient client) {
        log.info("Client disconnected: {}", client.getSessionId().toString());
        CLIENTS.remove(client.getSessionId());
    }
}