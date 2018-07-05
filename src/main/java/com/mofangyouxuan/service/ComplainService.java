package com.mofangyouxuan.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.utils.HttpUtils;
import com.mofangyouxuan.utils.PageCond;

/**
 * 用户投诉调用处理
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
	private static String complainDealUrl;
	private static String complainRevisitUrl;
	
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
	@Value("${mfyx.complain-partner-delete-url}")
	public void setComplainPartnerDeleteUrl(String url) {
		complainPartnerDeleteUrl = url;
	}
	@Value("${mfyx.complain-deal-url}")
	public void setComplainDealUrl(String url) {
		complainDealUrl = url;
	}
	@Value("${mfyx.complain-revisit-url}")
	public void setComplainRevisitUrl(String url) {
		complainRevisitUrl = url;
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
		params.put("oprId", userId);
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
	 * 保存用户的合作伙伴投诉信息
	 
	 * @param partnerId
	 * @param cplanId
	 * @param content
	 * @param upPartnerId
	 * @param phone
	 * @param operator
	 * @param passwd 
	 * @return {errcode,errmsg}
	 */
	public static JSONObject savePartnerComplain(Integer partnerId,Integer cplanId,String content,Integer upPartnerId,String phone,Integer operator,String passwd) {
		String url = mfyxServerUrl + complainPartnerSaveUrl;
		url = url.replace("{partnerId}", partnerId +"");
	
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("oprId", operator);
		params.put("oprPid", partnerId);
		params.put("passwd", passwd);
		params.put("cplanId", cplanId);
		params.put("partnerId", upPartnerId);
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
	 * 删除对上级合作伙伴的投诉信息
	 * @param partnerId
	 * @param cplainId
	 * @param operator
	 * @param passwd  
	 * @return {errcode,errmsg}
	 */
	public static JSONObject deletePartnerComplain(Integer partnerId,Integer cplainId,Integer operator,String passwd) {
		String url = mfyxServerUrl + complainPartnerDeleteUrl;
		url = url.replace("{partnerId}", partnerId +"");
		url = url.replace("{cplainId}", cplainId +"");
		Map<String, Object> params = new HashMap<String,Object>();
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
	 * 保存投诉处理信息 
	 * @param operator
	 * @param passwd
	 * @param logId
	 * @param content	处理结果内容
	 * @return
	 */
	public static JSONObject dealComplain(Integer operator,String passwd,
			Integer logId,String content) {
		String url = mfyxServerUrl + complainDealUrl;
	
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("operator", operator);
		params.put("passwd", passwd);
		params.put("logId", logId);
		params.put("content", content);
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
	 * 保存投诉处理信息 
	 * @param operator
	 * @param passwd
	 * @param logId		投诉记录ID
	 * @param content	回访内容
	 * @param result		回访结果：0-须再次处理，2：完成
	 * @return
	 */
	public static JSONObject revisitComplain(Integer operator,String passwd,
			Integer logId,String content,String result) {
		String url = mfyxServerUrl + complainRevisitUrl;
	
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("operator", operator);
		params.put("passwd", passwd);
		params.put("logId", logId);
		params.put("content", content);
		params.put("result", result);
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
	 * @param jsonSearchParams	查询条件:{oprId,oprPid,goodsId, partnerId,status,phone,beginCreateTime,endCreateTime,beginDealTime,endDealTime,beginRevisitTime,endRevisitTime}
	 * @param jsonSortParams		排序条件:{createTime:"N#0/1",dealTime:"N#0/1",revisitTime:"N#0/1"}
	 * @param jsonPageCond		分页信息:{begin:, pageSize:}
	 * @return {errcode,errmsg,pageCond,datas:[{...}...]}
	 */
	public static JSONObject getAll(JSONObject search,JSONObject sorts,PageCond pageCond) {
		String url = mfyxServerUrl + complainGetAllUrl;
	
		Map<String, Object> params = new HashMap<String,Object>();
		if(search == null) {
			search = new JSONObject();
		}
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
