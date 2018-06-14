package com.mofangyouxuan.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.dto.UserBasic;
import com.mofangyouxuan.utils.HttpUtils;

/**
 * 用户与会员服务，主要用于向服务中心发送请求
 * @author jeekhan
 *
 */
@Component 
public class UserService {
	
	private static String tmpFileDir;
	private static String mfyxServerUrl;
	private static String userBasicGetUrl;
	private static String userBasicGetByIdUrl;
	private static String userBasicCreateUrl;
	private static String userBasicUpdateUrl;
	private static String userSpreadQrCodeUrl;
	private static String userHeadimgUploadUrl;
	private static String userHeadimgShowUrl;
	
	private static String userPhoneUpdUrl;
	private static String userEmailUpdUrl;
	private static String userPwdResetUrl;
	private static String userPwdUpdUrl;
	
	private static String messmpServerUrl;
	private static String phoneVeriCodeGetUrl;
	private static String emailVeriCodeGetUrl;
	
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

	@Value("${mfyx.user-basic-getbyid-url}")
	public void setUserBasicGetByIdUrl(String url) {
		userBasicGetByIdUrl = url;
	}
	
	@Value("${mfyx.user-basic-update-url}")
	public void setUserBasicUpdateUrl(String url) {
		userBasicUpdateUrl = url;
	}
	
	@Value("${mfyx.user-basic-create-url}")
	public void setUserBasicCreateUrl(String url) {
		userBasicCreateUrl = url;
	}
	
	@Value("${mfyx.user-spread-qrcode-url}")
	public void setSpreadQrCodeUrl(String url) {
		userSpreadQrCodeUrl = url;
	}
	
	
	@Value("${mfyx.user-headimg-upload-url}")
	public void setUserHeadimgUploadUrl(String url) {
		userHeadimgUploadUrl = url;
	}
	@Value("${mfyx.user-headimg-show-url}")
	public void setUserHeadimgShowUrl(String url) {
		userHeadimgShowUrl = url;
	}	
	
	@Value("${mfyx.user-phone-upd-url}")
	public void setUserPhoneUpdUrl(String url) {
		userPhoneUpdUrl = url;
	}
	@Value("${mfyx.user-email-upd-url}")
	public void setUserEmailUpdUrl(String url) {
		userEmailUpdUrl = url;
	}
	
	@Value("${mfyx.user-pwd-reset-url}")
	public void setUserPwdResetUrl(String url) {
		userPwdResetUrl = url;
	}
	@Value("${mfyx.user-pwd-upd-url}")
	public void setUserPwdUpdUrl(String url) {
		userPwdUpdUrl = url;
	}
	
	@Value("${mfyx.messmp-server-url}")
	public void setMessmpServerUrl(String url) {
		messmpServerUrl = url;
	}
	
	@Value("${mfyx.phone-vericode-get-url}")
	public void setPhoneVeriCodeGetUrl(String url) {
		phoneVeriCodeGetUrl = url;
	}
	
	@Value("${mfyx.email-vericode-get-url}")
	public void setEmailVeriCodeGetUrl(String url) {
		emailVeriCodeGetUrl = url;
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
	 * 获取用户基本信息
	 * @param userId
	 * @return
	 */
	public static UserBasic getUserBasicById(Integer userId) {
		UserBasic userBasic = null;
		String url = mfyxServerUrl + userBasicGetByIdUrl;
		url = url.replace("{userId}", userId+"");
		String strRet = HttpUtils.doPost(mfyxServerUrl + userBasicGetUrl);
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
	 * 更新用户基本信息
	 * @param userBasic
	 * @return
	 */
	public static String updateUserBasic(UserBasic basic) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("nickname", basic.getNickname());
		params.put("birthday", basic.getBirthday());
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
		String strRet = HttpUtils.doPost(mfyxServerUrl + userSpreadQrCodeUrl, params);
		JSONObject jsonRet = JSONObject.parseObject(strRet);
		return jsonRet;
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
	
	/**
	 * 更新用户电话
	 * @param userId
	 * @param oldCeriCode	旧手机验证码（初始为空）
	 * @param newPhone		新手机号码
	 * @param newVeriCode	新手机验证码
	 * @return {errcode:0,errmsg:"ok"} 
	 */
	public static JSONObject updPhone(Integer userId ,String oldVeriCode,
			String newPhone,String newVeriCode) {
		String url = mfyxServerUrl + userPhoneUpdUrl;
		url = url.replace("{userId}", userId +"");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("oldVeriCode", oldVeriCode);
		params.put("newPhone", newPhone);
		params.put("newVeriCode", newVeriCode);
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
	 * 更新邮箱
	 * @param userId
	 * @param oldCeriCode	旧邮箱验证码（初始为空）
	 * @param newEmail		新邮箱号码
	 * @param newVeriCode	新邮箱验证码
	 * @return {errcode:0,errmsg:"ok"} 
	 */
	public static JSONObject updEmail(Integer userId ,String oldVeriCode,
			String newEmail,String newVeriCode) {
		String url = mfyxServerUrl + userEmailUpdUrl;
		url = url.replace("{userId}", userId +"");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("oldVeriCode", oldVeriCode);
		params.put("newEmail", newEmail);
		params.put("newVeriCode", newVeriCode);
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
	 * 获取邮箱验证码
	 * @param email		邮箱
	 * @return {errcode:0,errmsg:"ok"} 
	 */
	public static JSONObject getEmialVeriCode(String email) {
		String url = messmpServerUrl + emailVeriCodeGetUrl;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("email", email);
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
	 * 获取手机验证码
	 * @param phone		手机号码
	 * @return {errcode:0,errmsg:"ok"} 
	 */
	public static JSONObject getPhoneVeriCode(String phone) {
		String url = messmpServerUrl + phoneVeriCodeGetUrl;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("phone", phone);
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
	 * 重设用户密码（忘记或新设）
	 * @param userId
	 * @param type	重置媒介
	 * @return {errcode:0,errmsg:"ok"} 
	 */
	public static JSONObject resetPwd(Integer userId ,String type) {
		String url = mfyxServerUrl + userPwdResetUrl;
		url = url.replace("{userId}", userId +"");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("type", type);
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
	 * 更新用户密码
	 * @param userId
	 * @param oldPwd		旧密码
	 * @param newPwd		新密码
	 * @return {errcode:0,errmsg:"ok"} 
	 */
	public static JSONObject updPwd(Integer userId ,String oldPwd,String newPwd) {
		String url = mfyxServerUrl + userPwdUpdUrl;
		url = url.replace("{userId}", userId +"");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("oldPwd", oldPwd);
		params.put("newPwd", newPwd);
		String strRet = HttpUtils.doPostSSL(url, params);
		try {
			JSONObject jsonRet = JSONObject.parseObject(strRet);
			return jsonRet;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
