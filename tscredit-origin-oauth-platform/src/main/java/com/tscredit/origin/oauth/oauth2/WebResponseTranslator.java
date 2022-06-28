package com.tscredit.origin.oauth.oauth2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

public class WebResponseTranslator implements WebResponseExceptionTranslator {
    public ResponseEntity translate(Exception e) throws Exception {
        if (e instanceof InternalAuthenticationServiceException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new OAuth2Exception("账号密码错误"));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new OAuth2Exception(e.getMessage()));
    }
}