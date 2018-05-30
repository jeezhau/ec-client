package com.mofangyouxuan.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.wx.utils.HttpUtils;
import com.mofangyouxuan.wx.utils.PageCond;

/**
 * 收货人信息接口调用处理
 * @author jeekhan
 *
 */
@Component
public class ComplainService {
	
	private static String mfyxServerUrl;
	private static String complainGetAllUrl;
	private static String complainOrderSaveUrl;
	private static String complainOrderDeleteUrl;
	private static String complainPartnerSaveUrl;
	private static String complainPartnerDeleteUrl;
	
	@Value("${mfyx.mfyx-server-url}")
	public void setMfyxServerUrl(String mfyxServerUrl) {
		ComplainService.mfyxServerUrl = mfyxServerUrl;
	}

	@Value("${mfyx.complain-getall-url}")
	public void setComplainGetAllUrl(String url) {
		complainGetAllUrl = url;
	}
	@Value("${mfyx.complain-order-save-url}")
	public void setComplainOrderSaveUrl(String url) {
		complainOrderSaveUrl = url;
	}
	@Value("${mfyx.complain-order-delete-url}")
	public void setComplainOrderDeleteUrl(String url) {
		complainOrderDeleteUrl = url;
	}
	@Value("${mfyx.complain-partner-save-url}")
	public void setComplainPartnerSaveUrl(String url) {
		complainPartnerSaveUrl = url;
	}
	@Value("${mfyx.complain-order-delete-url}")
	public void setComplainPartnerDeleteUrl(String url) {
		complainPartnerDeleteUrl = url;
	}


	/**
	 * 保存用户的订单投诉信息
	 * @param userId
	 * @param cplanId
	 * @param content
	 * @param orderId
	 * @param phone
	 * @return {errcode,errmsg}
	 */
	public static JSONObject saveOrderComplain(Integer userId,Integer cplanId,String content,String orderId,String phone) {
		String url = mfyxServerUrl + complainOrderSaveUrl;
		url = url.replace("{userId}", userId +"");
	
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		params.put("cplanId", cplanId);
		params.put("orderId", orderId);
		params.put("content", content);
		params.put("cpType", "1");
		params.put("phone", phone);
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
	 * 保存用户的合作伙伴投诉信息
	 * @param vipId
	 * @param cplanId
	 * @param content
	 * @param partnerId
	 * @param phone
	 * @return {errcode,errmsg}
	 */
	public static JSONObject savePartnerComplain(Integer vipId,Integer cplanId,String content,Integer partnerId,String phone) {
		String url = mfyxServerUrl + complainPartnerSaveUrl;
		url = url.replace("{vipId}", vipId +"");
	
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("userId", vipId);
		params.put("cplanId", cplanId);
		params.put("partnerId", partnerId);
		params.put("content", content);
		params.put("cpType", "2");
		params.put("phone", phone);
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
	 * 用户删除自己的订单投诉信息
	 * @param userId
	 * @param cplainId
	 * @return {errcode,errmsg}
	 */
	public static JSONObject deleteOrderComplain(Integer userId,Integer cplainId) {
		String url = mfyxServerUrl + complainOrderDeleteUrl;
		url = url.replace("{userId}", userId +"");
		url = url.replace("{cplainId}", cplainId +"");
		Map<String, Object> params = new HashMap<String,Object>();
		
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
	 * 用户删除自己的订单投诉信息
	 * @param vipId
	 * @param cplainId
	 * @return {errcode,errmsg}
	 */
	public static JSONObject deletePartnerComplain(Integer vipId,Integer cplainId) {
		String url = mfyxServerUrl + complainPartnerDeleteUrl;
		url = url.replace("{vipId}", vipId +"");
		url = url.replace("{cplainId}", cplainId +"");
		Map<String, Object> params = new HashMap<String,Object>();
		
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
	 * 获取用户的所有投诉信息
	 * 
	 * @param jsonSearchParams	查询条件:{userId,goodsId, partnerId,status,phone,beginCreateTime,endCreateTime,beginDealTime,endDealTime,beginRevisitTime,endRevisitTime}
	 * @param jsonSortParams		排序条件:{createTime:"N#0/1",dealTime:"N#0/1",revisitTime:"N#0/1"}
	 * @param jsonPageCond		分页信息:{begin:, pageSize:}
	 * @return {errcode,errmsg,pageCond,datas:[{...}...]}
	 */
	public static JSONObject getUsersAll(Integer userId,JSONObject search,JSONObject sorts,PageCond pageCond) {
		String url = mfyxServerUrl + complainGetAllUrl;
	
		Map<String, Object> params = new HashMap<String,Object>();
		if(search == null) {
			search = new JSONObject();
		}
		search.put("userId", userId);
		if(sorts == null) {
			sorts = new JSONObject();
			sorts.put("createTime", "1#1");
		}
		if(pageCond == null) {
			pageCond = new PageCond(0,30);
		}
		params.put("jsonSearchParams", search.toJSONString());
		params.put("jsonSortParams", sorts.toJSONString());
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
