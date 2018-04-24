package com.mofangyouxuan.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.dto.UserBasic;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.wx.utils.HttpUtils;

/**
 * 用户与会员服务，主要用于向服务中心发送请求
 * @author jeekhan
 *
 */
@Component 
public class UserVipService {
	
	
	private static String mfyxServerUrl;
	private static String userBasicGetUrl;
	private static String userBasicUpdateUrl;
	private static String spreadQrCodeUrl;
	private static String vipBasicGetUrl;
	
	@Value("${mfyx.mfyx-server-url}")
	public void setMfyxServerUrl(String url) {
		mfyxServerUrl = url;
	}
	
	@Value("${mfyx.user-basic-get-url}")
	public void setUserBasicGetUrl(String url) {
		userBasicGetUrl = url;
	}
	
	@Value("${mfyx.user-basic-update-url}")
	public void setUserBasicUpdateUrl(String url) {
		userBasicUpdateUrl = url;
	}
	
	@Value("${mfyx.spread-qrcode-url}")
	public void setSpreadQrCodeUrl(String url) {
		spreadQrCodeUrl = url;
	}
	
	@Value("${mfyx.vip-basic-get-url}")
	public void setVipBasicGetUrl(String url) {
		vipBasicGetUrl = url;
	}
	
	/**
	 * 获取用户基本信息
	 * @param openId
	 * @return
	 */
	public static UserBasic getUserBasic(String openId) {
		UserBasic userBasic = null;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("openId", openId);
		String strRet = HttpUtils.doPost(mfyxServerUrl + userBasicGetUrl, params);
		try {
			JSONObject ret = JSONObject.parseObject(strRet);
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			if(ret.containsKey("id")) {//成功
//				userBasic = new UserBasic();
//				userBasic.setOpenId(openId);
//				
//				userBasic.setBirthday((ret.has("birthday") && !ret.isNull("birthday")) ? sdf1.parse(ret.getString("birthday")):null);
//				userBasic.setCity((ret.has("city") && !ret.isNull("city")) ? ret.getString("city"):"");
//				userBasic.setCountry((ret.has("country") && !ret.isNull("country")) ? ret.getString("country"):"");
//				userBasic.setEmail((ret.has("email") && !ret.isNull("email")) ? ret.getString("email"):"");
//				userBasic.setFavourite((ret.has("favourite") && !ret.isNull("favourite")) ? ret.getString("favourite"):"");
//				userBasic.setHeadimgurl((ret.has("headimgurl") && !ret.isNull("headimgurl")) ? ret.getString("headimgurl"):"");
//				userBasic.setId(ret.getInt("id"));
//				userBasic.setIntroduce((ret.has("introduce") && !ret.isNull("introduce")) ? ret.getString("introduce"):"");
//				userBasic.setNickname((ret.has("nickname") && !ret.isNull("nickname")) ? ret.getString("nickname"):"");
//				userBasic.setOpenId(openId);
//				userBasic.setPasswd("");
//				userBasic.setPhone((ret.has("phone") && !ret.isNull("phone")) ? ret.getString("phone"):"");
//				userBasic.setProfession((ret.has("profession") && !ret.isNull("profession")) ? ret.getString("profession"):"");
//				userBasic.setProvince((ret.has("province") && !ret.isNull("province")) ? ret.getString("province"):"");
//				//userBasic.setRegistTime(sdf2.parse(ret.getString("registTime")));
//				userBasic.setRegistType(ret.getString("registType"));
//				
//				userBasic.setSenceId((ret.has("senceId") && !ret.isNull("senceId"))?ret.getInt("senceId"):null);
//				userBasic.setSex((ret.has("sex") && !ret.isNull("sex")) ? ret.getString("sex"):"");
//				userBasic.setStatus(ret.getString("status"));
//				userBasic.setUnionId((ret.has("unionId") && !ret.isNull("unionId")) ? ret.getString("unionId"):"");
//				//userBasic.setUpdateTime(sdf2.parse(ret.getString("updateTime")));
//				
//				userBasic.setJSON(strRet);
				userBasic = JSONObject.toJavaObject(ret, UserBasic.class);
			}
		}catch(Exception e) {
			e.printStackTrace();
			userBasic = null;
		}
		
		return userBasic;
	}
	
	/**
	 * 获取会员基本信息
	 * @param openId
	 * @return
	 */
	public static VipBasic getVipBasic(String openId) {
		VipBasic vipBasic = null;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("openId", openId);
		String strRetVip = HttpUtils.doPost(mfyxServerUrl + vipBasicGetUrl, params);
		try {
			JSONObject ret = JSONObject.parseObject(strRetVip);
			if(ret.containsKey("vipId")) {//成功
				vipBasic = JSONObject.toJavaObject(ret, VipBasic.class);
//				vipBasic.setVipId(ret.getInt("vipId"));
//				vipBasic.setBalance(new BigDecimal(ret.getDouble("balance")));
//				vipBasic.setFreeze(new BigDecimal(ret.getDouble("freeze")));
//				vipBasic.setIsPartner(ret.getString("isPartner"));
//				vipBasic.setScores(ret.getInt("scores"));
//				vipBasic.setStatus(ret.getString("status"));
			}
		}catch(Exception e) {
			e.printStackTrace();
			vipBasic = null;
		}
		return vipBasic;
	}
	
	/**
	 * 更新用户基本信息
	 * @param userBasic
	 * @return
	 */
	public static String updateUserBasic(UserBasic basic) {
		String registType = "2";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("registType", registType);
		params.put("openId", basic.getOpenId());
		params.put("nickname", basic.getNickname());
		params.put("birthday", basic.getBirthday());
		params.put("phone", basic.getPhone());
		params.put("sex", basic.getSex());
		params.put("province", basic.getProvince());
		params.put("city", basic.getCity());
		params.put("favourite", basic.getFavourite());
		params.put("profession", basic.getProfession());
		params.put("introduce", basic.getIntroduce());
		String strRet = HttpUtils.doPost(mfyxServerUrl + userBasicUpdateUrl, params);
		return strRet;
	}
	
	/**
	 * 获取用户推广信息
	 * 1、取已推广用户数量；
	 * 2、获取最新推广二维码；
	 * @param create		是否强制重新生成:1-是，0-否
	 * @param map
	 * @return {errcode:0,errmsg:'ok',showurl:''}
	 * @throws JSONException 
	 */
	public static JSONObject getSpreadQrCode(String openId,String create) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("openId", openId);
		if(create != null && "1".equals(create.trim())){
			params.put("create", "1");
		}
		String strRet = HttpUtils.doPost(mfyxServerUrl + spreadQrCodeUrl, params);
		JSONObject jsonRet = JSONObject.parseObject(strRet);
		return jsonRet;
	}

}
