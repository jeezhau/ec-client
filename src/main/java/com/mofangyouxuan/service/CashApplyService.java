package com.mofangyouxuan.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.dto.CashApply;
import com.mofangyouxuan.utils.HttpUtils;
import com.mofangyouxuan.utils.ObjectToMap;
import com.mofangyouxuan.utils.PageCond;

/**
 * 合作伙伴员工服务调用处理
 * @author jeekhan
 *
 */
@Component
public class CashApplyService {
	private static String mfyxServerUrl;
	private static String cashGetAllUrl;
	private static String cashApplyUrl;
	private static String cashDeleteUrl;
	private static String cashUpdateStatUrl;
	
	@Value("${mfyx.mfyx-server-url}")
	public void setMfyxServerUrl(String mfyxServerUrl) {
		CashApplyService.mfyxServerUrl = mfyxServerUrl;
	}

	@Value("${mfyx.cash-getall-url}")
	public void setCashGetAllUrl(String url) {
		cashGetAllUrl = url;
	}
	@Value("${mfyx.cash-apply-url}")
	public void setCashApplyUrl(String url) {
		cashApplyUrl = url;
	}
	@Value("${mfyx.cash-delete-url}")
	public void setCashDeleteUrl(String url) {
		cashDeleteUrl = url;
	}
	@Value("${mfyx.cash-update-stat-url}")
	public void setCashUpdatePwdUrl(String url) {
		cashUpdateStatUrl = url;
	}
	

	/**
	 * 保存用户的订单投诉信息
	 * @param partnerId
	 * @param staff
	 * @param oprId
	 * @param passwd
	 * @return {errcode,errmsg}
	 */
	public static JSONObject cashApply(CashApply apply,String passwd) {
		String url = mfyxServerUrl + cashApplyUrl;
		url = url.replace("{vipId}", apply.getVipId() +"");
	
		Map<String, Object> params = new HashMap<String,Object>();
		String[] excludeFields = {"applyTime","status","updateOpr","updateTime","memo","applyId"};
		params = ObjectToMap.object2Map(apply,excludeFields,false);
		params.put("passwd", passwd);
		String strRet = HttpUtils.doPost(url, params);
		JSONObject jsonRet = new JSONObject();
		try {
			jsonRet = JSONObject.parseObject(strRet);
			if(!jsonRet.containsKey("errcode")) {
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "出现系统访问错误，错误信息：" + strRet);
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}
		return jsonRet;
	}
	

	/**
	 * 删除提现申请记录
	 * @param vipId
	 * @param passwd
	 * @param applyId
	 * @return {errcode,errmsg}
	 */
	public static JSONObject deleteApply(Integer vipId,String passwd,String applyId) {
		String url = mfyxServerUrl + cashDeleteUrl;
		url = url.replace("{vipId}", vipId +"");
	
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("applyId", applyId);
		params.put("passwd", passwd);
		String strRet = HttpUtils.doPost(url, params);
		JSONObject jsonRet = new JSONObject();
		try {
			jsonRet = JSONObject.parseObject(strRet);
			if(!jsonRet.containsKey("errcode")) {
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "出现系统访问错误，错误信息：" + strRet);
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}
		return jsonRet;
	}
	

	/**
	 * 提现处理 
	 * @param vipId
	 * @param applyId
	 * @param stat
	 * @param memo
	 * @param operator
	 * @param passwd
	 * @return
	 */
	public static JSONObject updaeStat(Integer vipId,String applyId,
			String stat,String memo,Integer operator,String passwd) {
		String url = mfyxServerUrl + cashUpdateStatUrl;
		
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("vipId", vipId);
		params.put("applyId", applyId);
		params.put("stat", stat);
		params.put("memo", memo);
		params.put("operator", operator);
		params.put("passwd", passwd);
		String strRet = HttpUtils.doPost(url, params);
		JSONObject jsonRet = new JSONObject();
		try {
			jsonRet = JSONObject.parseObject(strRet);
			if(!jsonRet.containsKey("errcode")) {
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "出现系统访问错误，错误信息：" + strRet);
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}
		return jsonRet;
	}
	
	
		
	/**
	 * 查询指定查询条件、排序条件、分页条件的信息；
	 * @param jsonSearchParams	查询条件:{applyId,vipId,cashType,accountType,idNo,accountNo,accountName,accountBank,channelType,applyOpr,updateOpr,status,beginApplyTime,endApplyTime}
	 * @param jsonPageCond		分页信息:{begin:, pageSize:}
	 * @return {errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
	 */
	public static JSONObject getAll(JSONObject search,PageCond pageCond) {
		String url = mfyxServerUrl + cashGetAllUrl;
		Map<String, Object> params = new HashMap<String,Object>();
		if(search == null) {
			search = new JSONObject();
		}
		if(pageCond == null) {
			pageCond = new PageCond(0,30);
		}
		params.put("jsonSearchParams", search.toJSONString());
		params.put("jsonPageCond", JSONObject.toJSONString(pageCond));
		String strRet = HttpUtils.doPost(url, params);
		JSONObject jsonRet = new JSONObject();
		try {
			jsonRet = JSONObject.parseObject(strRet);
			if(!jsonRet.containsKey("errcode")) {
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "出现系统访问错误，错误信息：" + strRet);
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}
		return jsonRet;
	}

}


