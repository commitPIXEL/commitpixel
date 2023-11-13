package com.ssafy.realrealfinal.pixelms.api.pixel.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.ssafy.realrealfinal.pixelms.api.pixel.dto.SocketClientInfo;
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
    private final int SCALE = 512;
    private final String CREDIT = "credit";
    private final String PIXEL = "pixel";
    private final String IS_PIXEL_SUCCESS = "isPixelSuccess";
    private final String AVAILABLE_CREDIT = "availableCredit";
    private final String URL = "url";

    // 연결된 클라이언트의 Websocket 세션이 key, {providerId, SocketIoClient}가 value
    private static final ConcurrentHashMap<UUID, SocketClientInfo> CLIENTS = new ConcurrentHashMap<>();

    /**
     * 스프링 애플리케이션 컨텍스트가 초기화되거나 새로 고쳐질 때 서버를 시작
     */
    @EventListener(ContextRefreshedEvent.class)
    public void start() {
        try {
            server.start();
        } catch (Exception e) {
            // TODO: 로깅 또는 재시도 로직
        }
    }

    /**
     * 클라이언트가 최초로 연결될 때 실행되는 메서드 CLIENTS 맵에 sessionId, providerId, client 저장 idNameMap 맵에
     * providerId, githubNickname 저장
     *
     * @param client
     */
    @OnConnect
    public void onConnect(SocketIOClient client) {
        log.info("New client connected: {}", client.getSessionId().toString());

        String accessToken = client.getHandshakeData().getSingleUrlParam("Authorization");
        // 닉네임이 바뀌는 경우는 이미 새롭게 로그인을 하고 프론트에 새로운 닉네임이 있는 상태
        // 그 닉네임을 최초 연결 때 보내주는 것이기 때문에 idNameMap에 put할 때 replace 된다
        String githubNickname = client.getHandshakeData().getSingleUrlParam("githubNickname");
        Integer providerId;
        // 비회원을 위해 임시로 providerId를 세팅
        if (accessToken == null || accessToken.isEmpty() || accessToken.equals("")) {
            providerId = -1;
        } else {
            // header로 온 accessToken을 auth로 feign 요청을 보내서 providerId를 얻음
            providerId = authFeignClient.withQueryString(accessToken);
        }
        CLIENTS.put(client.getSessionId(), new SocketClientInfo(providerId, client));
        // id-name redis 업데이트
        redisUtil.setData(providerId + ":name", githubNickname);
    }

    /**
     * "pixel" 이벤트가 발생했을 때 실행되는 메서드(사용자가 픽셀 한 개를 찍음) 1. rankms로 정보 보냄 2. 누적 사용 픽셀 업데이트 3. 모든 사용자에게
     * 변경사항 보냄
     *
     * @param client
     * @param pixelInfo 픽셀 한개의 정보 [x, y, r, g, b, url, githubNickname]
     */
    @OnEvent("pixel")
    public void onPixelEvent(SocketIOClient client, List pixelInfo) {
        log.info("Pixel event received: {}", pixelInfo);
        // 사용자가 없을 때
        if (CLIENTS == null || CLIENTS.size() == 0) {
            return;
        }

        Integer providerId = CLIENTS.get(client.getSessionId()).getProviderId();

        // 비회원이면 return
        if (providerId == -1) {
            return;
        }

        // 사용 가능 픽셀이 0개일 때
        if(pixelService.getAvailableCredit(providerId) == 0) {
            // 픽셀 정보 업데이트 실패!
            client.sendEvent(IS_PIXEL_SUCCESS, false);
            return;
        }

        // pixel redis 업데이트 & Rank에 kafka로 정보 보냄
        pixelService.updatePixelAndSendRank(providerId, pixelInfo);

        // 현재 클라이언트의 usedPixel redis 값 변경 후 클라이언트에게 반환
        pixelService.updateUsedPixel(providerId);
        Integer availableCredit = pixelService.getAvailableCredit(providerId);
        client.sendEvent(AVAILABLE_CREDIT, availableCredit);

        // 나를 제외한 모든 사용자에게 픽셀 변경 사항을 보내줌
        for (SocketClientInfo clientInfo : CLIENTS.values()) {
            SocketIOClient clientSession = clientInfo.getSocketIOClient();
            if (!client.getSessionId().equals(clientSession.getSessionId())
                    && clientSession.isChannelOpen()) {
                clientSession.sendEvent(PIXEL, pixelInfo);
            }
        }
        // 픽셀 정보 성공적으로 업데이트됨
        client.sendEvent(IS_PIXEL_SUCCESS, true);

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

        String index = String.valueOf(
            (Integer) pixelInfo.get(0) * SCALE + (Integer) pixelInfo.get(1));
        PixelInfoRes pixelInfoRes = pixelService.getUrlAndName(index);
        client.sendEvent(URL, pixelInfoRes);
    }

    /**
     * 클라이언트가 연결을 끊을 때 실행되는 메서드
     *
     * @param client
     */
    @OnDisconnect
    public void onDisconnectEvent(SocketIOClient client) {
        log.info("Client disconnected: {}", client.getSessionId().toString());
        CLIENTS.remove(client.getSessionId());
    }
}