package com.tscredit.origin.uaa.oauth2.handler;

import com.alibaba.fastjson.JSONObject;
import com.tscredit.origin.uaa.entity.dto.LoginResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录成功处理器
 */
@Slf4j
@Component("LoginSuccessHandler")
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
		log.info("【AppLoginInSuccessHandler】 onAuthenticationSuccess authentication={}", authentication);
		LoginResultDTO loginResult = new LoginResultDTO();
		loginResult.setSuccess(true);
		loginResult.setMessage("登录成功");
		try {
			HttpSession session = request.getSession(false);
			SavedRequest savedRequest =  (SavedRequest)session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
			if (null != savedRequest)
			{
				String targetUrlParameter = this.getTargetUrlParameter();
				if (!this.isAlwaysUseDefaultTargetUrl() && (targetUrlParameter == null || !StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
					this.clearAuthenticationAttributes(request);
					String targetUrl = savedRequest.getRedirectUrl();
					this.logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
					loginResult.setTargetUrl(targetUrl);
				} else {
					loginResult.setTargetUrl("/index");
				}
				response.setContentType("application/json;charset=UTF-8");
				response.getWriter().write(JSONObject.toJSONString(loginResult));
			}
			else
			{
				loginResult.setTargetUrl("/index");
				response.setContentType("application/json;charset=UTF-8");
				response.getWriter().write(JSONObject.toJSONString(loginResult));
			}
		}
		catch (Exception e)
		{
			loginResult.setTargetUrl("/index");
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(JSONObject.toJSONString(loginResult));
		}
	}

}
