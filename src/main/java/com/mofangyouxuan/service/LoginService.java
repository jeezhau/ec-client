package com.mofangyouxuan.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.utils.HttpUtils;

/**
 * 会员服务，主要用于向服务中心发送请求
 * @author jeekhan
 *
 */
@Component 
public class LoginService {
	
	private static String mfyxServerUrl;

	private static String loginUin;
	private static String loginPin;

	
	@Value("${mfyx.mfyx-server-url}")
	public void setMfyxServerUrl(String url) {
		mfyxServerUrl = url;
	}
	
	@Value("${mfyx.login-user-in}")
	public void setLoginUinUrl(String url) {
		loginUin = url;
	}
	@Value("${mfyx.login-partner-in}")
	public void setLoginPinUrl(String url) {
		loginPin = url;
	}
	
	
	/**
	 * 普通用户登录 
	 * @param userId
	 * @param source
	 * @param ip
	 * @param referer
	 * @param sessionid
	 * @param passwd
	 * @return {errcode,errmsg,restCnt,userBasic,vipBasic}
	 */
	public static JSONObject userLogin(String userId,String source,String ip,String referer,String sessionid,String passwd) {
		String url = mfyxServerUrl + loginUin;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		String term = "";
		if("wx".equals(source)) {
			term = "1";
		}else if("ios".equals(source) || "apk".equals(source)) {
			term = "2";
		}else if("pc".equals(source)) {
			term = "3";
		}
		params.put("source", term);
		params.put("ip", ip);
		params.put("referer", referer);
		params.put("sessionid", sessionid);
		params.put("passwd", passwd);
		try {
			String strRet = HttpUtils.doPost(url, params);
			JSONObject jsonRet = JSONObject.parseObject(strRet);
			return jsonRet;
		}catch(Exception e) {
			e.printStackTrace();
			JSONObject jsonRet = new JSONObject();
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
			jsonRet.put("errcode", -1);
			return jsonRet;
		}
	}
	


	/**
	 * 合作伙伴登录 
	 * @param userId
	 * @param staffTp
	 * @param partnerId
	 * @param source
	 * @param ip
	 * @param referer
	 * @param sessionid
	 * @param passwd
	 * @return {errcode,errmsg,restCnt,vipBasic,myPartner,mySettle,staff}
	 */
	public static JSONObject partnerLogin(Integer userId,String staffTp,Integer partnerId,
			String source,String ip,String referer,String sessionid,String passwd) {
		String url = mfyxServerUrl + loginPin;
		Map<String,Object> params = new HashMap<String,Object>();
		String term = "";
		if("wx".equals(source)) {
			term = "1";
		}else if("ios".equals(source) || "apk".equals(source)) {
			term = "2";
		}else if("pc".equals(source)) {
			term = "3";
		}
		params.put("source", term);
		params.put("userId", userId);
		params.put("staffTp", staffTp);
		params.put("partnerId", partnerId);
		params.put("ip", ip);
		params.put("referer", referer);
		params.put("sessionid", sessionid);
		params.put("passwd", passwd);
		try {
			String strRet = HttpUtils.doPost(url, params);
			JSONObject jsonRet = JSONObject.parseObject(strRet);
			return jsonRet;
		}catch(Exception e) {
			e.printStackTrace();
			JSONObject jsonRet = new JSONObject();
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
			jsonRet.put("errcode", -1);
			return jsonRet;
		}
	}
	
	
}
