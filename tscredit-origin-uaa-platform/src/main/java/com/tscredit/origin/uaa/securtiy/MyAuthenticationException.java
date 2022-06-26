package com.tscredit.origin.uaa.securtiy;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class MyAuthenticationException extends AuthenticationException {
    /**
     * 异常信息
     */
    private String errorMsg;

    /**
     * 错误码
     */
    private String code;


    private MyAuthenticationException(String errorMsg) {
        super(errorMsg);
        this.code = errorMsg.substring(0, 4);
        this.errorMsg = errorMsg.substring(5);
    }

    private MyAuthenticationException(Integer code, String errorMsg) {
        super(code + "_" + errorMsg);
        this.code = String.valueOf(code);
        this.errorMsg = errorMsg;
    }

    public MyAuthenticationException setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }


    public MyAuthenticationException setCode(String code) {
        this.code = code;
        return this;
    }

    /**
     * 抛出逻辑异常
     */
    public static MyAuthenticationException errorMessage(String errorMsg) {
        return new MyAuthenticationException(errorMsg);
    }
}