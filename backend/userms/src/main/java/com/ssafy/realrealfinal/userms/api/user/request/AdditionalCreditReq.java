package com.ssafy.realrealfinal.userms.api.user.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AdditionalCreditReq {

    private Integer providerId;
    private Integer additionalCredit;

    @Builder
    public AdditionalCreditReq(Integer providerId, Integer additionalCredit) {
        this.providerId = providerId;
        this.additionalCredit = additionalCredit;
    }
}
