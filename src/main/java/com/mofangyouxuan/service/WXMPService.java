package com.mofangyouxuan.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.wx.utils.HttpUtils;

/**
 * 访问微信管理平台提供的服务
 * @author jeekhan
 *
 */
@Component
public class WXMPService {
	
	private static String wxmpServerUrl;
	
	private static String jsapiSignUrl;
	
	@Value("${wxmp.wxmp-server-url}")
	public void setWXMPSercerUrl(String url) {
		wxmpServerUrl = url;
	}
	@Value("${wxmp.jsapi-sign-url}")
	public void setJsApiSignUrl(String url) {
		jsapiSignUrl = url;
	}
	
	/**
	 * 获取指定url对应的签名
	 * @param url
	 * @return {"errcode":0,"errmsg",:"ok","signature":""}
	 */
	public static JSONObject getSignature(String url ,Long timestamp,String nonceStr) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("url", url);
		params.put("timestamp", timestamp);
		params.put("nonceStr", nonceStr);
		String strRet = HttpUtils.doPost(wxmpServerUrl + jsapiSignUrl, params);
		JSONObject jsonRet = JSONObject.parseObject(strRet);
		return jsonRet;
	}

}
