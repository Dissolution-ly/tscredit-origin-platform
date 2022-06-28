package com.tscredit.origin.oauth.entity.dto;

import lombok.Data;

@Data
public class SmsVerificationDTO {

    private String smsCode;

    private String phoneNumber;

    private String code;
}
