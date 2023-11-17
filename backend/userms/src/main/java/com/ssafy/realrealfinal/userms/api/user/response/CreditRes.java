package com.ssafy.realrealfinal.userms.api.user.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class CreditRes {

    private Integer totalCredit;
    private Integer availablePixel;
}
