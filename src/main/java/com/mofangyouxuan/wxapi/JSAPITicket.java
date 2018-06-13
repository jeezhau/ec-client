package com.mofangyouxuan.wxapi;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.utils.HttpUtils;
import com.mofangyouxuan.utils.SignUtils;

public class JSAPITicket {
	private static Logger log = LoggerFactory.getLogger(JSAPITicket.class);
	private static String JSAPI_TICKET = null;		//访问凭证
	private static long LASTUPDTIME = 0L;		//上次获取时间
	private static long EXPIRESIN = 0L;			//有效时间
	
	/**
	 * 获取调用微信JS接口的临时票据：JSAPI_TICKET
	 * @throws JSONException 
	 */
	public static String getJSAPITicket(){
		long currTime = System.currentTimeMillis()/1000;
		long needTime = LASTUPDTIME + EXPIRESIN + 60;//提前60s更新
		if(JSAPI_TICKET == null || currTime >= needTime){
			String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
			url = url.replaceAll("ACCESS_TOKEN", AccessToken.getAccessToken());
			//{"errcode":0,"errmsg":"ok","ticket":"bxLdikRXVbTPdHSM05e5u5sUoXNKd8-41ZO3MhKoyN5OfkWITDGgnr2fwJ0m9E8NYzWKVZvdVtaUgWvsdshFKA","expires_in":7200}
			String result = HttpUtils.doGet(url);
			JSONObject json = JSONObject.parseObject(result);
			if(json != null){
				if(json.containsKey("ticket")){
					JSAPI_TICKET = json.getString("ticket");
					EXPIRESIN = json.getLong("expires_in");
					LASTUPDTIME = System.currentTimeMillis()/1000;
					log.info("JSAPI_TICKET获取返回成功：" + result);
					return JSAPI_TICKET;
				}else if(json.containsKey("errmsg")){
					log.info("JSAPI_TICKET获取返回失败，失败信息：" + json.getString("errmsg"));
				}
			}
		}
		return JSAPI_TICKET;
	}
	
	//生成JS-SDK权限验证的签名
	public static String signature(String url,long timestamp,String noncestr){
		String jsapi_ticket = getJSAPITicket();
		String string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr="+ noncestr +"&timestamp="+ timestamp + "&url=" + url;
		String ret = null;
		try {
			ret = SignUtils.encodeSHAHex(string1);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println(string1);
		return ret;
	}
}
