package com.ssafy.realrealfinal.imagems.api.image.service;

import com.ssafy.realrealfinal.imagems.api.image.feignInterface.ImageClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {
    ImageClient imageClient;

}
