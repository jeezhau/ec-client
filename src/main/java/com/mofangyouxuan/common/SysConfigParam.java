package com.mofangyouxuan.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("unused")
public class SysConfigParam {
	
	//公众号ID
	public static String PUBLIC_ID;
	//公众号APPID
	public static String APP_ID;
	//公众号APPSECRET
	public static String APPSECRET;
	//公众号TOKEN
	public static String TOKEN;
	
	@Value("${wxmp.PUBLIC-USER-ID}")
	public void setPublicId(String id) {
		PUBLIC_ID = id;
	}
	@Value("${wxmp.APPID}")
	public void setAppId(String appid) {
		APP_ID = appid;
	}
	@Value("${wxmp.APPSECRET}")
	public void setAppSecret(String secret) {
		APPSECRET = secret;
	}
	
	@Value("${wxmp.TOKEN}")
	public void setToken(String token) {
		TOKEN = token;
	}

}
