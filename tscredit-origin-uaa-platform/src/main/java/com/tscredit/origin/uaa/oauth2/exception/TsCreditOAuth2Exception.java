package com.tscredit.origin.uaa.oauth2.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;


/**
 * 自定义Oauth异常拦截处理器
 */
@JsonSerialize(using = TsCreditOAuth2ExceptionSerializer.class)
public class TsCreditOAuth2Exception extends OAuth2Exception {

    public TsCreditOAuth2Exception(String msg, Throwable t) {
        super(msg, t);
    }

    public TsCreditOAuth2Exception(String msg) {
        super(msg);
    }
}
