package com.tscredit.origin.oauth.service.impl;

import lombok.SneakyThrows;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * 客户端信息
 */
@Component
public class TsCreditClientDetailsServiceImpl extends JdbcClientDetailsService {

	public TsCreditClientDetailsServiceImpl(DataSource dataSource) {
		super(dataSource);
	}

	/**
	 * 缓存客户端信息
	 *
	 * @param clientId 客户端id
	 */
	@Override
	@SneakyThrows
	public ClientDetails loadClientByClientId(String clientId) {
		return super.loadClientByClientId(clientId);
	}
}
