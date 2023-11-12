package com.ssafy.realrealfinal.pixelms.api.pixel.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CreditRes {

    private Integer totalCredit;
    private Integer availablePixel;
}
