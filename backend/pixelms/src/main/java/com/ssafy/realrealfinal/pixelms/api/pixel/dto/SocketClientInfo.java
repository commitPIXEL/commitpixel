package com.ssafy.realrealfinal.pixelms.api.pixel.dto;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SocketClientInfo {

    private Integer providerId;
    private SocketIOClient socketIOClient;

}
