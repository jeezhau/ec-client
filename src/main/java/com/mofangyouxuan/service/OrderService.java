package com.mofangyouxuan.service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
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
	private static String orderCheckDataUrl;
	private static String orderSearchUrl;
	private static String orderCountPartiByStatus;

	
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
	public void setOrderGetUrl(String url) {
		OrderService.orderGetUrl = url;
	}
	@Value("${mfyx.order-checkdata-url}")
	public void setOrderCehckDataUrl(String url) {
		OrderService.orderCheckDataUrl = url;
	}
	@Value("${mfyx.order-search-url}")
	public void setOrderSearchUrl(String url) {
		OrderService.orderSearchUrl = url;
	}
	@Value("${mfyx.order-count-partiby-status-url}")
	public void setOrderCountPartiByStatusUrl(String url) {
		OrderService.orderCountPartiByStatus = url;
	}
	
	/**
	 * 根据ID获取订单信息
	 * @param orderId
	 * @return {"errcode":-1,"errmsg":"错误信息",order:{...}} 
	 */
	public static JSONObject getOrder(BigInteger orderId) {
		String url = mfyxServerUrl + orderGetUrl;
		url = url.replace("{orderId}", orderId+"") ;
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
	public static JSONObject createOrder(Order order,Integer userId) {
		String url = mfyxServerUrl + orderCreateUrl;
		url = url.replace("{userId}", userId + "");
		Map<String, Object> params = new HashMap<String,Object>();
		String[] excludeFields = {"createTime","status",
				"logisticsComp","logisticsNo","sendTime","signTime","signUser",
				"scoreLogistics","scoreGoods","scoreMerchant","appraiseInfo","appraiseStatus",
				"aftersalesReason","aftersalesResult","appraiseTime","aftersalesApplyTime","aftersalesDealTime"};
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
	public static JSONObject updateOrder(Order order,Integer userId) {
		String url = mfyxServerUrl + orderUpdateUrl;
		url = url.replace("{userId}", userId + "");
		Map<String, Object> params = new HashMap<String,Object>();
		String[] excludeFields = {"createTime","status",
				"logisticsComp","logisticsNo","sendTime","signTime","signUser",
				"scoreLogistics","scoreGoods","scoreMerchant","appraiseInfo","appraiseStatus",
				"aftersalesReason","aftersalesResult","appraiseTime","aftersalesApplyTime","aftersalesDealTime"};
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
	 * 订单数据检查并获取是否可配送客户所选收货地区
	 * 1、如果不可配送则不可下单，并给出提示；
	 * 2、如果可以配送，则给出所有的可选模式以及对应的费用；
	 * @param recvId		收货信息ID
	 * @param goodsId	商品ID
	 * @param goodsSpec		商品数量
	 * @return {"errcode":0,"errmsg":"ok",match:[{postageId:'',mode:'',carrage:''}...]}
	 */
	public static JSONObject checkOrderData(Integer userId, Long recvId, Long goodsId,String goodsSpec) {
		String url = mfyxServerUrl + orderCheckDataUrl;
		url = url.replace("{userId}", userId + "");
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		params.put("recvId", recvId);
		params.put("goodsId", goodsId);
		params.put("goodsSpec", goodsSpec);
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
	 * @param jsonSearchParams
	 * @param jsonSortParams
	 * @param jsonPageCond
	 * @return {errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
	 */
	public static JSONObject searchOrders(String jsonSearchParams,String jsonSortParams,String jsonPageCond) {
		String url = mfyxServerUrl + orderSearchUrl;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("jsonSearchParams", jsonSearchParams);
		params.put("jsonSortParams", jsonSortParams);
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
	 * 分状态统计用户或合作伙伴下的订单信息
	 * @param userId
	 * @param goodsId
	 * @param partinerId
	 * @return {errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
	 */
	public static JSONObject countPartiByStatus(Integer userId,Long goodsId,Integer partnerId) {
		String url = mfyxServerUrl + orderCountPartiByStatus;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		params.put("goodsId", goodsId);
		params.put("partnerId", partnerId);
		String strRet = HttpUtils.doPost(url, params);
		try {
			JSONObject jsonRet = JSONObject.parseObject(strRet);
			return jsonRet;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
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
