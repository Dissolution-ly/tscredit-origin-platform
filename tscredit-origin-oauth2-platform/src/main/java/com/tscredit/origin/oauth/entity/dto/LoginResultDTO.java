package com.tscredit.origin.oauth.entity.dto;

import lombok.Data;


@Data
public class LoginResultDTO {

	private boolean success;

	private String message;

	private String targetUrl;
}
