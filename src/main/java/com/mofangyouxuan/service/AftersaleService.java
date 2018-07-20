package com.mofangyouxuan.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
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
public class AftersaleService {
	private static String mfyxServerUrl;
	private static String refundUrl;
	private static String exchangeUrl;
	private static String dealUrl;
	private static String getUrl;
	private static String searchUrl;
	

	@Value("${mfyx.aftersale-refund-url}")
	public void setRefundUrl(String url) {
		AftersaleService.refundUrl = url;
	}
	@Value("${mfyx.aftersale-exchange-url}")
	public void setExchangeUrl(String url) {
		AftersaleService.exchangeUrl = url;
	}
	
	@Value("${mfyx.mfyx-server-url}")
	public void setMfyxServerUrl(String mfyxServerUrl) {
		AftersaleService.mfyxServerUrl = mfyxServerUrl;
	}
	
	@Value("${mfyx.aftersale-get-url}")
	public void setGetUrl(String url) {
		AftersaleService.getUrl = url;
	}

	@Value("${mfyx.aftersale-search-url}")
	public void setSearchUrl(String url) {
		AftersaleService.searchUrl = url;
	}
	
	@Value("${mfyx.aftersale-deal-url}")
	public void setDealUrl(String url) {
		AftersaleService.dealUrl = url;
	}
	
	/**
	 * 根据订单ID获取售后信息
	 * 
	 * @param orderId
	 * @return {"errcode":-1,"errmsg":"错误信息",order:{...}} 
	 */
	public static JSONObject getAftersale(String orderId) {
		String url = mfyxServerUrl + getUrl;
		url = url.replace("{orderId}", orderId) ;
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
	 * @param jsonSearchParams	查询条件:{orderId,goodsId, partnerId,beginApplyTime,endApplyTime,beginDealTime,endDealTime}
	 * @param jsonPageCond		分页信息:{begin:, pageSize:}
	 * @return {errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
	 */
	public static JSONObject searchAfs(String jsonSearchParams,String jsonPageCond) {
		String url = mfyxServerUrl + searchUrl;
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
	 * 买家申请退款，并退货
	 * @param user
	 * @param order
	 * @param type		退款类型(1-未收到货，3-签收退款：品质与描述问题或无理由退货)
	 * @param content	退款理由，签收退货包含快递信息{reason,dispatchMode,logisticsComp,logisticsNo}
	 * @param passwd		会员操作密码
	 * @return
	 */
	public static JSONObject applyRefund(UserBasic user,Order order,
			String type,JSONObject content,String passwd) {
		JSONObject jsonRet = new JSONObject();
		Map<String,Object> params = new HashMap<String,Object>();
		//向服务中心发送申请
		String url = mfyxServerUrl + refundUrl;
		url = url.replace("{userId}", user.getUserId() + "");
		url = url.replace("{orderId}", order.getOrderId() + "");
		params.put("type", type);
		params.put("content", content.toJSONString());
		params.put("passwd", passwd);
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
	 * 买家申请换货，并退货
	 * @param user
	 * @param order
	 * @param content		换货理由，包含快递信息{reason,dispatchMode,logisticsComp,logisticsNo}
	 * @return {errcode,errmsg}
	 */
	public static JSONObject exchange(UserBasic user,Order order,JSONObject content) {
		JSONObject jsonRet = new JSONObject();
		Map<String,Object> params = new HashMap<String,Object>();
		//向服务中心发送申请
		String url = mfyxServerUrl + exchangeUrl;
		url = url.replace("{userId}", user.getUserId() + "");
		url = url.replace("{orderId}", order.getOrderId() + "");
		params.put("content", content.toJSONString());
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
	 * 卖家更新售后信息
	 * @param partner
	 * @param order
	 * @param nextStat	下一个状态／处理结果（52:已收到退货、核验中，53:核验不通过、协商解决，54:已重新发货、待收货，62：已收到退货、核验中，63:核验不通过、协商解决，64:同意退款，申请资金回退）
	 * @param content	评价内容，json格式{reason,dispatchMode,logisticsComp,logisticsNo}
	 * @return {errcode,errmsg}
	 */
	public static JSONObject updAfterSales(PartnerBasic partner,Order order,
			String nextStat,JSONObject content,String passwd,Integer updateOpr) {
		JSONObject jsonRet = new JSONObject();
		Map<String,Object> params = new HashMap<String,Object>();
		//向服务中心发送申请
		String url = mfyxServerUrl + dealUrl;
		url = url.replace("{partnerId}", partner.getPartnerId() + "");
		url = url.replace("{orderId}", order.getOrderId() + "");
		params.put("nextStat", nextStat);
		params.put("content", content.toJSONString());
		params.put("passwd", passwd);
		params.put("currUserId", updateOpr);
		String strRet = HttpUtils.doPost(url, params);
		try {
			jsonRet = JSONObject.parseObject(strRet);
			return jsonRet;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
