package com.tscredit.origin.oauth.securtiy;

import com.tscredit.common.response.ActionMessage;
import com.tscredit.common.response.ErrorMessage;
import com.tscredit.common.util.JsonUtils;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AjaxAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        // 设置错误信息
        Object result = ActionMessage.error(ErrorMessage.USER_PASSWORD);
        if(exception instanceof BadCredentialsException){
            result = ActionMessage.error(exception.getMessage());
        } else if (exception instanceof DisabledException || exception instanceof LockedException) {
            result = ActionMessage.error(ErrorMessage.USER_CLOSE);
        } else if (exception instanceof AccountExpiredException) {
            result = ActionMessage.error(ErrorMessage.USER_OVERDUE);
        } else if (exception instanceof AuthenticationServiceException) {
            result = ActionMessage.error(ErrorMessage.USER_NULL);
        } else if (exception instanceof MyAuthenticationException) {
            MyAuthenticationException exception1 = (MyAuthenticationException) exception;
            result = ActionMessage.error().code(exception1.getCode()).msg(exception1.getMessage());
        }

        // 响应信息
        PrintWriter out = response.getWriter();
        out.write(JsonUtils.convertObject2Json(result));
        out.flush();
        out.close();
    }

}
