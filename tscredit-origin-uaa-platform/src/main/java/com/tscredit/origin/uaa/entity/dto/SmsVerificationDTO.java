package com.tscredit.origin.uaa.entity.dto;

import lombok.Data;

@Data
public class SmsVerificationDTO {

    private String smsCode;

    private String phoneNumber;

    private String code;
}
