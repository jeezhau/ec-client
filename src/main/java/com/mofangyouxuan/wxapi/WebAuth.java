package com.mofangyouxuan.wxapi;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.dto.UserBasic;
import com.mofangyouxuan.utils.HttpUtils;



/**
 * 网页授权获取用户基本信息
 * @author Jee Khan
 *
 */
@Component
public class WebAuth {
	private static Logger log = LoggerFactory.getLogger(WebAuth.class);
	
	public static String APPID ;		//公众号的唯一标识 
	private static String APPSECRET;
	private static String LANG = "zh_CN";		//国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语 
	private static String SCOPE = "snsapi_userinfo";
	public static String STATE;
	
	@Value("${wx.APPID}")
	public void setAPPID(String appId) {
		APPID = appId;
	}
	@Value("${wx.APPSECRET}")
	public void setAppSecret(String appSecret) {
		APPSECRET = appSecret;
	}
	@Value("${wx.STATE}")
	public void setState(String state) {
		STATE = state;
	}
	
	/**
	 * 用户同意授权，获取code；
	 *  生成请求授权页面
	 * https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
	 * @param redirect_uri	授权后重定向的回调链接地址，请使用urlencode对链接进行处理
	 * @param scope			应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息） 
	 * @param state			重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
	 * @throws UnsupportedEncodingException 
	 */
	public static String getCode(String redirect_uri ,String state) throws UnsupportedEncodingException{
		String redUrl = URLEncoder.encode(redirect_uri, "utf-8");
		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?"
				+ "appid=" + APPID
				+ "&redirect_uri=" + redUrl
				+ "&response_type=code"
				+ "&scope=" + SCOPE
				+ "&state=" + state
				+ "#wechat_redirect";
		return url;
	}
	
	

	/**
	 * 通过code换取网页授权access_token
	 * 1、如果用户以前获取的有效，则直接返回；
	 * @param code	
	 * @param forceUpd	是否强制重新获取授权码
	 * @param loginLog	用户的登录信息，其中的auth拷贝自用户的上次登录的auth
	 * @return
	 * { "access_token":"ACCESS_TOKEN",
	 * "expires_in":7200,
	 * "refresh_token":"REFRESH_TOKEN",
	 * "openid":"OPENID",
	 * "scope":"SCOPE",
	 * "update_time": ""}
	 */
	public static JSONObject getAccessToken(String code,boolean forceUpd,JSONObject oldAuth) {
		Long currTime = System.currentTimeMillis()/1000; //秒
		
		String url = null;
		if(oldAuth == null || forceUpd){//第一次登录系统、强制重新获取
			url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
		}else if(oldAuth != null) {	//accesstoken、refreshtoken失效重新获取
			Long lastUpdTime = oldAuth.getLong("update_time");
			Long expireInTime = oldAuth.getLong("expires_in");
			String refresh_token = oldAuth.getString("refresh_token");
			long nextUpdTime = lastUpdTime + expireInTime + 10;//提前10s更新
			int days = (int) ((new Date().getTime()/1000 - lastUpdTime) / (3600*24));
			
			System.out.println("currTime:" + currTime);
			System.out.println("nextUpdTime:" + nextUpdTime);
			System.out.println("days:" + days);
			
			if(currTime < nextUpdTime && days <= 28) {	//直接返回以前的accesstoken 信息
				return oldAuth;
			}
			
			if(currTime >= nextUpdTime) {
				url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
				url = url.replaceAll("REFRESH_TOKEN", refresh_token);
			}
			
			if(days > 28) {
				url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
			}
		}
		url = url.replaceAll("APPID", APPID).replaceAll("SECRET", APPSECRET).replaceAll("CODE", code);
		String result  = HttpUtils.doGetSSL(url);
		JSONObject jsonRet = JSONObject.parseObject(result);
		if(jsonRet != null && jsonRet.containsKey("access_token")){
			jsonRet.put("update_time", currTime);//添加更新时间
			log.info("WEB_ACCESS_TOKEN获取返回成功：" + result);
		}else if(jsonRet.containsKey("errmsg")){
			log.info("WEB_ACCESS_TOKEN获取返回失败，失败信息：" + result);
		}
		return jsonRet;
	}
	
	/**
	 * 拉取用户信息(需scope为 snsapi_userinfo)
	 * @return
	 * @throws JSONException 
	 */
	public static UserBasic getUserInfo(JSONObject auth){
		String url= "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
		url = url.replaceAll("ACCESS_TOKEN", auth.getString("access_token")).replaceAll("OPENID", auth.getString("openid")).replaceAll("zh_CN", LANG);
		String result = HttpUtils.doGetSSL(url);
		JSONObject json = JSONObject.parseObject(result);
		if(json != null && json.containsKey("openid")) {
			UserBasic user = JSONObject.toJavaObject(json, UserBasic.class);
			user.setOpenId(json.getString("openid"));
			return user;
		}
		return null;
	}
	
	/**
	 * 检验授权凭证（access_token）是否有效
	 * @return
	 * @throws JSONException 
	 */
	public static boolean checkAuth(JSONObject auth) {
		String url= "https://api.weixin.qq.com/sns/auth?access_token=ACCESS_TOKEN&openid=OPENID";
		url = url.replaceAll("ACCESS_TOKEN", auth.getString("access_token")).replaceAll("OPENID", auth.getString("openid"));
		String result = HttpUtils.doGetSSL(url);
		JSONObject jsonRet = JSONObject.parseObject(result);
		if(jsonRet != null && jsonRet.containsKey("errcode") && jsonRet.getIntValue("errcode") == 0){
			return true;
		}else{
			return false;
		}
	}
}
