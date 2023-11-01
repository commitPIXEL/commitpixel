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
import com.ssafy.realrealfinal.pixelms.common.util.NicknameProviderUtil;
import com.ssafy.realrealfinal.pixelms.common.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketHandler {

    private final PixelService pixelService;
    private final RedisUtil redisUtil;
    private final NicknameProviderUtil nicknameProviderUtil;
    private final AuthFeignClient authFeignClient;
    private final SocketIOServer server;
    private final int SCALE = 512;

    // 연결된 클라이언트의 Websocket 세션이 key,
    private static final ConcurrentHashMap<UUID, SocketClientInfoDto> CLIENTS = new ConcurrentHashMap<>();

    /**
     * 스프링 애플리케이션 컨텍스트가 초기화되거나 새로 고쳐질 때 서버를 시작
     */
    @EventListener(ContextRefreshedEvent.class)
    public void start() {
        server.start();
    }

    /**
     * 클라이언트가 최초로 연결될 때 실행되는 메서드
     * @param client
     */
    @OnConnect
    public void onConnect(SocketIOClient client) {
        log.info("New client connected: {}", client.getSessionId().toString());

        String accessToken = client.getHandshakeData().getHttpHeaders().get("Authorization");
        // 비회원 테스트를 위해 임시로 providerId를 세팅
        if (accessToken == null || accessToken.isEmpty() || accessToken.equals("")) {
            CLIENTS.put(client.getSessionId(), new SocketClientInfoDto(-1, client));
        } else {
            Integer providerId = authFeignClient.withQueryString(accessToken);

            // TODO: pixelms에 <providerId, nickname> 맵을 하나 만들어 놓고 put
            CLIENTS.put(client.getSessionId(), new SocketClientInfoDto(providerId, client));
        }
    }

    /**
     * "pixel" 이벤트가 발생했을 때 실행되는 메서드(사용자가 픽셀 한 개를 찍음)
     * 1. rankms로 정보 보냄
     * 2. 누적 사용 픽셀 업데이트
     * 3. 모든 사용자에게 변경사항 보냄
     *
     * @param client
     * @param pixelInfo 픽셀 한개의 정보 [x, y, r, g, b, url, userName]
     */
    @OnEvent("pixel")
    public void onPixelEvent(SocketIOClient client, List pixelInfo) {
        log.info("Pixel event received: {}", pixelInfo);
        // 사용자가 없을 때
        if (CLIENTS == null || CLIENTS.size() == 0) {
            return;
        }

        // pixel redis 업데이트 & Rank에 kafka로 정보 보냄
        pixelService.updatePixelRedisAndSendRank(pixelInfo);

        // 나를 제외한 모든 사용자에게 픽셀 변경 사항을 보내줌
        for (SocketClientInfoDto clientInfo : CLIENTS.values()) {
            SocketIOClient clientSession = clientInfo.getSocketIOClient();
            if (!client.getSessionId().equals(clientSession.getSessionId()) && clientSession.isChannelOpen()) {
                clientSession.sendEvent("pixel", pixelInfo);
            }
        }

        Integer providerId = CLIENTS.get(client.getSessionId()).getProviderId();
        // 비회원이면 return
        if (providerId == -1) {
            return;
        }
        // 현재 클라이언트의 usedPixel redis 값 변경 후 클라이언트에게 반환
        pixelService.updateUsedPixel(providerId);
        Integer availableCredit = pixelService.getAvailableCredit(providerId);
        client.sendEvent("pixel", availableCredit);
        return;
    }

    /**
     * 픽셀 클릭 시 해당 url과 작성자를 반환
     *
     * @param client
     * @param pixelInfo 클릭한 픽셀의 위치 [x, y]
     */
    @OnEvent("url")
    public void onUrlEvent(SocketIOClient client, List pixelInfo) {
        log.info("Url event received: {}", pixelInfo);

        Integer index = (Integer) pixelInfo.get(0) * SCALE + (Integer) pixelInfo.get(1);
        String url = redisUtil.getStringData(String.valueOf(index), "url");
        String githubNickname = redisUtil.getStringData(String.valueOf(index), "id");
        PixelInfoRes pixelInfoRes = new PixelInfoRes(url, githubNickname);
        client.sendEvent("url", pixelInfoRes);
    }

    /**
     * 클라이언트가 연결을 끊을 때 실행되는 메서드
     * @param client
     */
    @OnDisconnect
    public void onDisconnectEvent(SocketIOClient client) {
        log.info("Client disconnected: {}", client.getSessionId().toString());
        CLIENTS.remove(client.getSessionId());
    }
}