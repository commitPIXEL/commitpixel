package com.ssafy.realrealfinal.pixelms.api.pixel.controller;

import com.ssafy.realrealfinal.pixelms.api.pixel.service.PixelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/pixel")
@RestController
public class PixelController {

    private final PixelService pixelService;

    public void consumeCreditEvent(Object object) {
        log.info("consumeCreditEvent start");

        pixelService.updateAndSendCredit(object);

        log.info("consumeCreditEvent end");
    }

    public void consumeSolvedAcEvent(Object object) {
        log.info("consumeSolvedAcEvent start");

        pixelService.updateAndSendCredit(object);

        log.info("consumeSolvedAcEvent end");
    }


}
