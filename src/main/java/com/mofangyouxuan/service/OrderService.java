package com.mofangyouxuan.service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.dto.Category;
import com.mofangyouxuan.dto.Goods;
import com.mofangyouxuan.dto.Order;
import com.mofangyouxuan.wx.utils.HttpUtils;
import com.mofangyouxuan.wx.utils.ObjectToMap;

/**
 * 订单管理服务
 * @author jeekhan
 *
 */
@Component
public class OrderService {
	private static String mfyxServerUrl;
	private static String orderCreateUrl;
	private static String orderUpdateUrl;
	private static String orderGetUrl;
	private static String orderGetAllNoUserUrl;
	private static String orderGetAllWithUserUrl;

	
	@Value("${mfyx.mfyx-server-url}")
	public void setMfyxServerUrl(String mfyxServerUrl) {
		OrderService.mfyxServerUrl = mfyxServerUrl;
	}
	
	@Value("${mfyx.order-create-url}")
	public void setOrderCreateUrl(String url) {
		OrderService.orderCreateUrl = url;
	}
	@Value("${mfyx.order-update-url}")
	public void setOrderUpdateUrl(String url) {
		OrderService.orderUpdateUrl = url;
	}
	@Value("${mfyx.order-get-url}")
	public void setOrderGetRrl(String url) {
		OrderService.orderGetUrl = url;
	}
	
	
	/**
	 * 根据ID获取订单信息
	 * @param orderId
	 * @return {"errcode":-1,"errmsg":"错误信息",order:{...}} 
	 */
	public static JSONObject getGoods(BigInteger orderId) {
		String url = mfyxServerUrl + orderGetUrl + orderId ;
		String strRet = HttpUtils.doGet(url);
		JSONObject jsonRet = null;
		try {
			jsonRet = JSONObject.parseObject(strRet);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return jsonRet;
	}
	
	/**
	 * 创建订单
	 * @param order
	 * @return {errcode:0,errmsg:'ok',orderId:111}
	 */
	public static JSONObject createGoods(Order order,Integer userId) {
		String url = mfyxServerUrl + orderCreateUrl;
		url = url.replace("{userId}", userId + "");
		Map<String, Object> params = new HashMap<String,Object>();
		String[] excludeFields = {"createTime","status",
				"logistics_comp","logistics_no","send_time","sign_time","sign_user",
				"score_logistics","score_goods","score_merchant","appraise_info","appraise_status",
				"aftersales_reason","aftersales_result"};
		params = ObjectToMap.object2Map(order,excludeFields,false);
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
	 * 更新订单
	 * @param order
	 * @return {errcode:0,errmsg:'ok',orderId:111}
	 */
	public static JSONObject updateGoods(Order order,Integer userId) {
		String url = mfyxServerUrl + orderUpdateUrl;
		url = url.replace("{userId}", userId + "");
		Map<String, Object> params = new HashMap<String,Object>();
		String[] excludeFields = {"createTime","status",
				"logistics_comp","logistics_no","send_time","sign_time","sign_user",
				"score_logistics","score_goods","score_merchant","appraise_info","appraise_status",
				"aftersales_reason","aftersales_result"};
		params = ObjectToMap.object2Map(order,excludeFields,false);
		String strRet = HttpUtils.doPost(url, params);
		try {
			JSONObject jsonRet = JSONObject.parseObject(strRet);
			return jsonRet;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
//	/**
//	 * @param hasPartner 是否包含合作伙伴数据
//	 * @param jsonSearchParams
//	 * @param jsonSortParams
//	 * @param jsonPageCond
//	 * @return {errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
//	 */
//	public static JSONObject searchGoods(boolean hasPartner,String jsonSearchParams,String jsonSortParams,String jsonPageCond) {
//		String url = mfyxServerUrl + (hasPartner==true?goodsGetallWithPartnerUrl:goodsGetallNopartnerUrl);
//		Map<String,Object> params = new HashMap<String,Object>();
//		params.put("jsonSearchParams", jsonSearchParams);
//		params.put("jsonSortParams", jsonSortParams);
//		params.put("jsonPageCond", jsonPageCond);
//		String strRet = HttpUtils.doPost(url, params);
//		try {
//			JSONObject jsonRet = JSONObject.parseObject(strRet);
//			return jsonRet;
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//	
//	/**
//	 * 
//	 * @param goodsIds
//	 * @param partnerId
//	 * @param newStatus
//	 * @return {errcode:0,errmsg:"ok"}
//	 */
//	public static JSONObject changeStatus(String goodsIds,Integer partnerId,String newStatus) {
//		String url = mfyxServerUrl + goodsChangeStatusUrl;
//		Map<String,Object> params = new HashMap<String,Object>();
//		params.put("goodsIds", goodsIds);
//		params.put("partnerId", partnerId);
//		params.put("newStatus", newStatus);
//		String strRet = HttpUtils.doPost(url, params);
//		try {
//			JSONObject jsonRet = JSONObject.parseObject(strRet);
//			return jsonRet;
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	/**
//	 * 
//	 * @param partnerId
//	 * @param goodsId
//	 * @param specDetail
//	 * @return {errcode:0,errmsg:"ok"}
//	 */
//	public static JSONObject changeSpec(Integer partnerId,Long goodsId,String specDetail) {
//		String url = mfyxServerUrl + goodsChangeSpecUrl;
//		Map<String,Object> params = new HashMap<String,Object>();
//		params.put("goodsId", goodsId);
//		params.put("partnerId", partnerId);
//		params.put("specDetail", specDetail);
//		String strRet = HttpUtils.doPost(url, params);
//		try {
//			JSONObject jsonRet = JSONObject.parseObject(strRet);
//			return jsonRet;
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	

	
}
