package com.mofangyouxuan.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.wx.utils.HttpUtils;

/**
 * 用户与会员服务，主要用于向服务中心发送请求
 * @author jeekhan
 *
 */
@Component 
public class VipService {
	
	private static String tmpFileDir;
	private static String mfyxServerUrl;

	private static String vipBasicGetUrl;
	private static String vipCashFlowGetAllUrl;
	private static String vipUpdPhoneUrl;
	private static String vipUpdEmailUrl;
	private static String vipUpdPwdUrl;
	private static String vipUpdActUrl;
	
	
	private static String messmpServerUrl;
	private static String vipPhoneVeriCodeGetUrl;
	private static String vipEmailVeriCodeGetUrl;

	
	@Value("${sys.tmp-file-dir}")
	public void setTmpFileDir(String url) {
		tmpFileDir = url;
	}
	
	@Value("${mfyx.mfyx-server-url}")
	public void setMfyxServerUrl(String url) {
		mfyxServerUrl = url;
	}
	
	@Value("${mfyx.vip-basic-get-url}")
	public void setVipBasicGetUrl(String url) {
		vipBasicGetUrl = url;
	}
	@Value("${mfyx.cash-flow-getall-url}")
	public void setChangeFlowGetAllUrl(String url) {
		vipCashFlowGetAllUrl = url;
	}
	@Value("${mfyx.vip-upd-pwd-url}")
	public void setVipUpdPwdUrl(String url) {
		vipUpdPwdUrl = url;
	}
	@Value("${mfyx.vip-upd-act-url}")
	public void setVipUpdActUrl(String url) {
		vipUpdActUrl = url;
	}
	@Value("${mfyx.vip-upd-phone-url}")
	public void setVipUpdPhoneUrl(String url) {
		vipUpdPhoneUrl = url;
	}
	@Value("${mfyx.vip-upd-email-url}")
	public void setVipUpdEmailUrl(String url) {
		vipUpdEmailUrl = url;
	}
	
	@Value("${mfyx.messmp-server-url}")
	public void setMessmpServerUrl(String url) {
		messmpServerUrl = url;
	}
	
	@Value("${mfyx.vip-phone-vericode-get-url}")
	public void setPhoneVeriCodeGetUrl(String url) {
		vipPhoneVeriCodeGetUrl = url;
	}
	
	@Value("${mfyx.vip-email-vericode-get-url}")
	public void setEmailVeriCodeGetUrl(String url) {
		vipEmailVeriCodeGetUrl = url;
	}
	
	
	/**
	 * 获取会员基本信息
	 * @param openId
	 * @return
	 */
	public static VipBasic getVipBasic(Integer vipId) {
		VipBasic vipBasic = null;
		String url = mfyxServerUrl + vipBasicGetUrl;
		url = url.replace("{vipId}", vipId+"");
		String strRetVip = HttpUtils.doPost(url);
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
	 * 查询会员资金流水信息
	 * @param jsonSearchParams
	 * @param jsonPageCond
	 * @return {errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
	 */
	public static JSONObject searchFlows(String jsonSearchParams,String jsonPageCond) {
		String url = mfyxServerUrl + vipCashFlowGetAllUrl;
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
	 * @param oldPwd		旧密码
	 * @param newPwd		新密码
	 * @return {errcode:0,errmsg:"ok"} 
	 */
	public static JSONObject updPayPwd(Integer vipId ,String oldPwd,String newPwd) {
		String url = mfyxServerUrl + vipUpdPwdUrl;
		url = url.replace("{vipId}", vipId +"");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("vipId", vipId);
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
	
	/**
	 * 更新会员提现账户信息
	 * 
	 * @param vipId
	 * @param idNo
	 * @param actNm
	 * @param actNo
	 * @param actBlk
	 * @param pwd
	 * @return
	 */
	public static JSONObject updAccount(Integer vipId,String idNo,String actNm,String actNo,String actBlk,String pwd) {
		String url = mfyxServerUrl + vipUpdActUrl;
		url = url.replace("{vipId}", vipId +"");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("idNo", idNo);
		params.put("actNm", actNm);
		params.put("actNo", actNo);
		params.put("actBlk", actBlk);
		params.put("pwd", pwd);
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
	 * 更新会员电话
	 * @param vipId
	 * @param oldCeriCode	旧手机验证码（初始为空）
	 * @param newPhone		新手机号码
	 * @param newVeriCode	新手机验证码
	 * @return {errcode:0,errmsg:"ok"} 
	 */
	public static JSONObject updPhone(Integer vipId ,String oldVeriCode,
			String newPhone,String newVeriCode) {
		String url = mfyxServerUrl + vipUpdPhoneUrl;
		url = url.replace("{vipId}", vipId +"");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("vipId", vipId);
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
	 * @param vipId
	 * @param oldCeriCode	旧邮箱验证码（初始为空）
	 * @param newEmail		新邮箱号码
	 * @param newVeriCode	新邮箱验证码
	 * @return {errcode:0,errmsg:"ok"} 
	 */
	public static JSONObject updEmail(Integer vipId ,String oldVeriCode,
			String newEmail,String newVeriCode) {
		String url = mfyxServerUrl + vipUpdEmailUrl;
		url = url.replace("{vipId}", vipId +"");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("vipId", vipId);
		params.put("oldVeriCode", oldVeriCode);
		params.put("newPhone", newEmail);
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
		String url = messmpServerUrl + vipEmailVeriCodeGetUrl;
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
		String url = messmpServerUrl + vipPhoneVeriCodeGetUrl;
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
}
