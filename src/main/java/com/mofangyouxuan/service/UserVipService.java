package com.mofangyouxuan.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONException;
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
	
	private static String tmpFileDir;
	private static String mfyxServerUrl;
	private static String userBasicGetUrl;
	private static String userBasicCreateUrl;
	private static String userBasicUpdateUrl;
	private static String spreadQrCodeUrl;
	private static String vipBasicGetUrl;
	private static String changeFlowGetAllUrl;
	private static String vipUpdPwdUrl;
	private static String vipUpdActUrl;
	private static String userHeadimgUploadUrl;
	private static String userHeadimgShowUrl;
	
	@Value("${sys.tmp-file-dir}")
	public void setTmpFileDir(String url) {
		tmpFileDir = url;
	}
	
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
	
	@Value("${mfyx.user-basic-create-url}")
	public void setUserBasicCreateUrl(String url) {
		userBasicCreateUrl = url;
	}
	
	@Value("${mfyx.spread-qrcode-url}")
	public void setSpreadQrCodeUrl(String url) {
		spreadQrCodeUrl = url;
	}
	
	@Value("${mfyx.vip-basic-get-url}")
	public void setVipBasicGetUrl(String url) {
		vipBasicGetUrl = url;
	}
	@Value("${mfyx.change-flow-getall-url}")
	public void setChangeFlowGetAllUrl(String url) {
		changeFlowGetAllUrl = url;
	}
	@Value("${mfyx.vip-upd-pwd-url}")
	public void setVipUpdPwdUrl(String url) {
		vipUpdPwdUrl = url;
	}
	@Value("${mfyx.vip-upd-act-url}")
	public void setVipUpdActUrl(String url) {
		vipUpdActUrl = url;
	}
	@Value("${mfyx.user-headimg-upload-url}")
	public void setUserHeadimgUploadUrl(String url) {
		userHeadimgUploadUrl = url;
	}
	@Value("${mfyx.user-headimg-show-url}")
	public void setUserHeadimgShowUrl(String url) {
		userHeadimgShowUrl = url;
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
			if(ret.containsKey("userId")) {//成功
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
	 * 更新用户基本信息
	 * @param userBasic
	 * @return
	 */
	public static JSONObject createUserBasic(UserBasic basic) {
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
		String strRet = HttpUtils.doPost(mfyxServerUrl + userBasicCreateUrl, params);
		JSONObject jsonRet = JSONObject.parseObject(strRet);
		return jsonRet;
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

	/**
	 * 查询会员资金流水信息
	 * @param jsonSearchParams
	 * @param jsonPageCond
	 * @return {errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
	 */
	public static JSONObject searchFlows(String jsonSearchParams,String jsonPageCond) {
		String url = mfyxServerUrl + changeFlowGetAllUrl;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("jsonSearchParams", jsonSearchParams);
		params.put("jsonPageCond", jsonPageCond);
		String strRet = HttpUtils.doPost(url, params);
		try {
			JSONObject jsonRet = JSONObject.parseObject(strRet);
			return jsonRet;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 更新会员资金操作密码
	 * @param vipId
	 * @param passwd		资金操作密码
	 * @return {errcode:0,errmsg:"ok"} 
	 */
	public static JSONObject updPayPwd(Integer vipId ,String passwd) {
		String url = mfyxServerUrl + vipUpdPwdUrl;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("vipId", vipId);
		params.put("passwd", passwd);
		String strRet = HttpUtils.doPostSSL(url, params);
		try {
			JSONObject jsonRet = JSONObject.parseObject(strRet);
			return jsonRet;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 更新会员提现账户信息
	 * @param vipId
	 * @param actNm
	 * @param actNo
	 * @param actBlk
	 * @return
	 */
	public static JSONObject updAccount(Integer vipId,String actNm,String actNo,String actBlk) {
		String url = mfyxServerUrl + vipUpdActUrl;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("vipId", vipId);
		params.put("actNm", actNm);
		params.put("actNo", actNo);
		params.put("actBlk", actBlk);
		String strRet = HttpUtils.doPostSSL(url, params);
		try {
			JSONObject jsonRet = JSONObject.parseObject(strRet);
			return jsonRet;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 头像照片上传
	 * @param imageFile
	 * @param userId
	 * @return {errcode:0,errmsg:""}
	 */
	public static JSONObject uploadHeadImg(File imageFile,Integer userId) {
		String url = mfyxServerUrl + userHeadimgUploadUrl;
		url = url.replace("{userId}", userId + "");
		Map<String,String> paramPairs = new HashMap<String,String>();
		String strRet = HttpUtils.uploadFile(url, imageFile, "image", paramPairs);
		try {
			JSONObject jsonRet = JSONObject.parseObject(strRet);
			return jsonRet;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取头像照片
	 * @param userId
	 * @return
	 */
	public static File showHeadimg(Integer userId) {
		String url = mfyxServerUrl + userHeadimgShowUrl;
		url = url.replace("{userId}", userId + "");
		File file = HttpUtils.downloadFile(tmpFileDir,url);
		return file;
	}
}
