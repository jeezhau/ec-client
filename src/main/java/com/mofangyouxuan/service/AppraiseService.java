package com.mofangyouxuan.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.dto.Order;
import com.mofangyouxuan.dto.PartnerBasic;
import com.mofangyouxuan.dto.UserBasic;
import com.mofangyouxuan.utils.HttpUtils;


/**
 * 订单管理服务
 * @author jeekhan
 *
 */
@Component
public class AppraiseService {
	private static String mfyxServerUrl;
	private static String appr2MchtUrl;
	private static String appr2UserUrl;
	private static String apprSearchUrl;
	private static String apprGetUrl;
	private static String apprReviewUrl;
	

	@Value("${mfyx.appraise-appr2mcht-url}")
	public void setAppr2MchtUrl(String appr2MchtUrl) {
		AppraiseService.appr2MchtUrl = appr2MchtUrl;
	}
	@Value("${mfyx.appraise-appr2user-url}")
	public void setAppr2UserUrl(String appr2UserUrl) {
		AppraiseService.appr2UserUrl = appr2UserUrl;
	}
	
	@Value("${mfyx.mfyx-server-url}")
	public void setMfyxServerUrl(String mfyxServerUrl) {
		AppraiseService.mfyxServerUrl = mfyxServerUrl;
	}
	
	@Value("${mfyx.appraise-get-url}")
	public void setAppraiseGetUrl(String url) {
		AppraiseService.apprGetUrl = url;
	}

	@Value("${mfyx.appraise-search-url}")
	public void setAppraiseSearchUrl(String url) {
		AppraiseService.apprSearchUrl = url;
	}
	
	@Value("${mfyx.appraise-review-url}")
	public void setApprReviewUrl(String url) {
		AppraiseService.apprReviewUrl = url;
	}
	
	/**
	 * 根据订单ID获取评价信息
	 * 
	 * @param orderId
	 * @return {"errcode":-1,"errmsg":"错误信息",order:{...}} 
	 */
	public static JSONObject getAppraise(String orderId,String object) {
		String url = mfyxServerUrl + apprGetUrl;
		url = url.replace("{orderId}", orderId) ;
		url = url.replace("{object}", object) ;
		Map<String,Object> params = new HashMap<String,Object>();
		String strRet = HttpUtils.doPost(url,params);
		JSONObject jsonRet = null;
		try {
			jsonRet = JSONObject.parseObject(strRet);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return jsonRet;
	}
	
	
	
	/**
	 * 查询指定查询条件、分页条件的信息；
	 * @param jsonSearchParams	查询条件:{orderId,object,goodsId, partnerId,status,beginUpdateTime,endUpdateTime}
	 * @param jsonPageCond		分页信息:{begin:, pageSize:}
	 * @return {errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
	 */
	public static JSONObject searchApprs(String jsonSearchParams,String jsonPageCond) {
		String url = mfyxServerUrl + apprSearchUrl;
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
	 * 买家评价商家
	 * @param user
	 * @param order
	 * @param scoreLogistics		物流评分
	 * @param scoreMerchangt		商家服务评分
	 * @param scoreGoods		商品描述评分
	 * @param content	评价内容
	 * @return {errcode,errmsg}
	 */
	public static JSONObject appr2Mcht(UserBasic user,Order order,
			Integer scoreLogistics,Integer scoreMerchant,Integer scoreGoods,String content) {
		JSONObject jsonRet = new JSONObject();
		Map<String,Object> params = new HashMap<String,Object>();
		//向服务中心发送申请
		String url = mfyxServerUrl + appr2MchtUrl;
		url = url.replace("{orderId}", order.getOrderId() + "");
		params.put("userId", user.getUserId());
		params.put("scoreLogistics", scoreLogistics);
		params.put("scoreMerchant", scoreMerchant);
		params.put("scoreGoods", scoreGoods);
		params.put("content", content);
		String strRet = HttpUtils.doPost(url, params);
		try {
			jsonRet = JSONObject.parseObject(strRet);
			return jsonRet;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 卖家对买家评价
	 * @param partner
	 * @param order
	 * @param score		评分
	 * @param content	评价内容
	 * @return {errcode,errmsg}
	 */
	public static JSONObject appr2User(PartnerBasic partner,String orderId,Integer score,String content,Integer updateOpr,String passwd) {
		JSONObject jsonRet = new JSONObject();
		Map<String,Object> params = new HashMap<String,Object>();
		//向服务中心发送申请
		String url = mfyxServerUrl + appr2UserUrl;
		url = url.replace("{orderId}", orderId);
		params.put("partnerId",  partner.getPartnerId());
		params.put("passwd", passwd);
		params.put("currUserId", updateOpr);
		params.put("score", score);
		params.put("content", content);
		String strRet = HttpUtils.doPost(url, params);
		try {
			jsonRet = JSONObject.parseObject(strRet);
			return jsonRet;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 订单评价审核与抽查
	 * 上级审批下级信息
	 * 1、上级可对下级进行审核；
	 * 2、顶级对所有合作伙伴进行最终审核；
	 * 3、仅顶级审核通过后才算通过；
	 * @param orderId	待审批商品ID
	 * @param review 	审批意见
	 * @param result 	审批结果：S-通过，R-拒绝
	 * @param rewPartnerId	审核者合作伙伴ID
	 * @param operator	审批人ID，为上级合作伙伴的员工用户ID
	 * @param passwd		审批人操作密码
	 * 
	 * @return {errcode:0,errmsg:"ok"}
	 * @throws JSONException
	 */
	public static JSONObject reviewAppr(String orderId,String review,String result,
			Integer rewPartnerId,Integer operator,String passwd){
		Map<String,Object> params = new HashMap<String,Object>();
		JSONObject jsonRet = new JSONObject();
		try {
			params.put("orderId", orderId);
			params.put("review", review);
			params.put("result", result);
			params.put("rewPartnerId", rewPartnerId);
			params.put("operator", operator);
			params.put("passwd", passwd);
			String url = mfyxServerUrl + apprReviewUrl;
			String strRet = HttpUtils.doPost(url, params);
			jsonRet = JSONObject.parseObject(strRet);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errmsg", "系统错误：" + strRet);
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errmsg", "系统异常：" + e.getMessage());
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
		}
		return jsonRet;
	}
}
