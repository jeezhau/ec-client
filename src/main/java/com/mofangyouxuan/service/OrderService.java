package com.mofangyouxuan.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.dto.Order;
import com.mofangyouxuan.dto.PartnerBasic;
import com.mofangyouxuan.dto.UserBasic;
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
	private static String orderCancelUrl;
	private static String orderPrepayUrl;
	private static String orderPayFinishUrl;
	private static String orderReadyUrl;
	private static String orderDeliveryUrl;
	private static String orderApplyRefundUrl;
	private static String orderExchangeUrl;
	private static String orderAppr2MchtUrl;
	private static String orderAppr2UserUrl;
	private static String orderAfterSalesUrl;
	private static String orderGetLogisticsUrl;
	private static String orderBalPaySubmitUrl;
	private static String orderGetPayFlowUrl;
	
	@Value("${mfyx.order-delivery-url}")
	public void setOrderDeliveryUrl(String orderDeliveryUrl) {
		OrderService.orderDeliveryUrl = orderDeliveryUrl;
	}
	@Value("${mfyx.order-refund-url}")
	public void setOrderApplyRefundUrl(String orderApplyRefundUrl) {
		OrderService.orderApplyRefundUrl = orderApplyRefundUrl;
	}
	@Value("${mfyx.order-exchange-url}")
	public void setOrderExchangeUrl(String orderExchangeUrl) {
		OrderService.orderExchangeUrl = orderExchangeUrl;
	}
	@Value("${mfyx.order-appr2mcht-url}")
	public void setOrderAppr2MchtUrl(String orderAppr2MchtUrl) {
		OrderService.orderAppr2MchtUrl = orderAppr2MchtUrl;
	}
	@Value("${mfyx.order-appr2user-url}")
	public void setOrderAppr2UserUrl(String orderAppr2UserUrl) {
		OrderService.orderAppr2UserUrl = orderAppr2UserUrl;
	}
	@Value("${mfyx.order-updaftersales-url}")
	public void setOrderAfterSalesUrl(String orderAfterSalesUrl) {
		OrderService.orderAfterSalesUrl = orderAfterSalesUrl;
	}
	@Value("${mfyx.order-logistics-url}")
	public void setOrderGetLogisticsUrl(String orderGetLogisticsUrl) {
		OrderService.orderGetLogisticsUrl = orderGetLogisticsUrl;
	}

	
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
	@Value("${mfyx.order-cancel-url}")
	public void setOrderCancelUrl(String url) {
		OrderService.orderCancelUrl = url;
	}
	@Value("${mfyx.order-prepay-url}")
	public void setOrderPrepayUrl(String url) {
		OrderService.orderPrepayUrl = url;
	}
	@Value("${mfyx.order-pay-finish-url}")
	public void setOrderPayFinishUrl(String url) {
		OrderService.orderPayFinishUrl = url;
	}
	
	@Value("${mfyx.order-ready-url}")
	public void setOrderReadyUrl(String url) {
		OrderService.orderReadyUrl = url;
	}
	@Value("${mfyx.order-balpay-submit-url}")
	public void setBalPaySubmitUrl(String url) {
		OrderService.orderBalPaySubmitUrl = url;
	}
	@Value("${mfyx.order-payflow-get-url}")
	public void setGetPayFlowUrl(String url) {
		OrderService.orderGetPayFlowUrl = url;
	}
	/**
	 * 根据ID获取订单信息
	 * 
	 * @param needReceiver
	 * @param needLogistics
	 * @param needAppr
	 * @param needAfterSales
	 * @param needGoodsAndUser
	 * @param orderId
	 * @return {"errcode":-1,"errmsg":"错误信息",order:{...}} 
	 */
	public static JSONObject getOrder(Boolean needReceiver,Boolean needLogistics,Boolean needAppr,
			Boolean needAfterSales,Boolean needGoodsAndUser,String orderId) {
		String url = mfyxServerUrl + orderGetUrl;
		url = url.replace("{orderId}", orderId+"") ;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("needReceiver", needReceiver);
		params.put("needLogistics", needLogistics);
		params.put("needAppr", needAppr);
		params.put("needAfterSales", needAfterSales);
		params.put("needGoodsAndUser", needGoodsAndUser);
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
	 * 
	 * @param jsonShowGroups	需要显示的字段分组:needReceiver,needLogistics,needAppr,needAfterSales,needGoodsAndUser
	 * @param jsonSearchParams
	 * @param jsonSortParams
	 * @param jsonPageCond
	 * @return {errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
	 */
	public static JSONObject searchOrders(String jsonShowGroups,String jsonSearchParams,String jsonSortParams,String jsonPageCond) {
		String url = mfyxServerUrl + orderSearchUrl;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("jsonShowGroups", jsonShowGroups);
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
	
	/**
	 * 取消订单
	 * @param orderId
	 * @param userId
	 * @return {errcode:0,errmsg:"ok"}
	 */
	public static JSONObject cancelOrder(Order order,UserBasic user) {
		String url = mfyxServerUrl + orderCancelUrl;
		url = url.replace("{userId}", order.getUserId() + "");
		url = url.replace("{orderId}", order.getOrderId() + "");
		Map<String,Object> params = new HashMap<String,Object>();
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
	 * 生成待付款订单的预付信息
	 * 1、如果是微信支付，则项微信支付发送预付单生成请求，成功返回后向服务中心申请生成预付单；
	 * 2、余额支付则直接完成付款；
	 * @param payType 支付方式
	 * @param order
	 * @param userId
	 * @return {errcode,errmsg,payType,appId,timeStamp,nonceStr,prepay_id,paySign}
	 */
	public static JSONObject createPay(Integer payType,Order order,UserBasic user,String ip) {
		JSONObject jsonRet = new JSONObject();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("payType", payType);
		params.put("userIp",ip);
		
		//向服务中心发送申请
		String url = mfyxServerUrl + orderPrepayUrl;
		url = url.replace("{userId}", user.getUserId() + "");
		url = url.replace("{orderId}", order.getOrderId() + "");
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
	 * 获取已经创建的支付流水
	 * @param order
	 * @param user
	 * @return {errcode,errmsg,payflow:{}}
	 */
	public static JSONObject getPayFlow(Order order,UserBasic user,String type) {
		JSONObject jsonRet = new JSONObject();
		Map<String,Object> params = new HashMap<String,Object>();
		
		//向服务中心发送申请
		String url = mfyxServerUrl + orderGetPayFlowUrl;
		url = url.replace("{userId}", user.getUserId() + "");
		url = url.replace("{orderId}", order.getOrderId() + "");
		url = url.replace("{type}", type);
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
	 * 发送余额支付请求
	 * @param orderId
	 * @param userId
	 * @param pwd 会员密码
	 * @return
	 */
	public static JSONObject submitBalPay(String orderId,Integer userId,String pwd) {
		JSONObject jsonRet = new JSONObject();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("passwd",pwd);
		
		//向服务中心发送申请
		String url = mfyxServerUrl + orderBalPaySubmitUrl;
		url = url.replace("{userId}", userId + "");
		url = url.replace("{orderId}", orderId + "");
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
	 * 发送支付完成请求
	 * @param order
	 * @param user
	 * @param status 客户端发送的支付状态
	 * @return
	 */
	public static JSONObject payFinish(Order order,UserBasic user,String status) {
		JSONObject jsonRet = new JSONObject();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("status",status);
		
		//向服务中心发送申请
		String url = mfyxServerUrl + orderPayFinishUrl;
		url = url.replace("{userId}", user.getUserId() + "");
		url = url.replace("{orderId}", order.getOrderId() + "");
		url = url.replace("{status}", status );
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
	 * 卖家准备发货或取消备货
	 * @param order
	 * @param user
	 * @param status 客户端发送的支付状态
	 * @return {errcode,errmsg}
	 */
	public static JSONObject readyGoods(Order order,PartnerBasic partner) {
		JSONObject jsonRet = new JSONObject();
		Map<String,Object> params = new HashMap<String,Object>();
		//向服务中心发送申请
		String url = mfyxServerUrl + orderReadyUrl;
		url = url.replace("{partnerId}", partner.getPartnerId() + "");
		url = url.replace("{orderId}", order.getOrderId() + "");
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
	 * 卖家发货设置物流信息：快递公司、单号
	 * @param partner
	 * @param order
	 * @param logisticsComp
	 * @param logisticsNo
	 * @return
	 */
	public static JSONObject deliveryGoods(PartnerBasic partner,Order order,
			String logisticsComp,String logisticsNo) {
		JSONObject jsonRet = new JSONObject();
		Map<String,Object> params = new HashMap<String,Object>();
		//向服务中心发送申请
		String url = mfyxServerUrl + orderDeliveryUrl;
		url = url.replace("{partnerId}", partner.getPartnerId() + "");
		url = url.replace("{orderId}", order.getOrderId() + "");
		params.put("logisticsComp", logisticsComp);
		params.put("logisticsNo", logisticsNo);
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
	 * 买家申请退款，并退货
	 * @param user
	 * @param order
	 * @param type		退款类型(1-未收到货，3-签收退款：品质与描述问题或无理由退货)
	 * @param reason		退款理由，签收退货包含快递信息{reason,dispatchMode,logisticsComp,logisticsNo}
	 * @param passwd		会员操作密码
	 * @return
	 */
	public static JSONObject applyRefund(UserBasic user,Order order,
			String type,String reason,String passwd) {
		JSONObject jsonRet = new JSONObject();
		Map<String,Object> params = new HashMap<String,Object>();
		//向服务中心发送申请
		String url = mfyxServerUrl + orderApplyRefundUrl;
		url = url.replace("{userId}", user.getUserId() + "");
		url = url.replace("{orderId}", order.getOrderId() + "");
		params.put("type", type);
		params.put("reason", reason);
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
	 * 查询订单的物流信息
	 * @param order
	 * @return {errcode,errmsg,order:{}}
	 */
	public static JSONObject getLogistics(Order order) {
		JSONObject jsonRet = new JSONObject();
		Map<String,Object> params = new HashMap<String,Object>();
		//向服务中心发送申请
		String url = mfyxServerUrl + orderGetLogisticsUrl;
		url = url.replace("{orderId}", order.getOrderId() + "");
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
	 * @param reason		换货理由，包含快递信息{reason,dispatchMode,logisticsComp,logisticsNo}
	 * @return {errcode,errmsg}
	 */
	public static JSONObject exchange(UserBasic user,Order order,String reason) {
		JSONObject jsonRet = new JSONObject();
		Map<String,Object> params = new HashMap<String,Object>();
		//向服务中心发送申请
		String url = mfyxServerUrl + orderExchangeUrl;
		url = url.replace("{userId}", user.getUserId() + "");
		url = url.replace("{orderId}", order.getOrderId() + "");
		params.put("reason", reason);
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
		String url = mfyxServerUrl + orderAppr2MchtUrl;
		url = url.replace("{userId}", user.getUserId() + "");
		url = url.replace("{orderId}", order.getOrderId() + "");
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
	public static JSONObject appr2User(PartnerBasic partner,Order order,Integer score,String content) {
		JSONObject jsonRet = new JSONObject();
		Map<String,Object> params = new HashMap<String,Object>();
		//向服务中心发送申请
		String url = mfyxServerUrl + orderAppr2UserUrl;
		url = url.replace("{partnerId}", partner.getPartnerId() + "");
		url = url.replace("{orderId}", order.getOrderId() + "");
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
	 * 卖家更新售后信息
	 * @param partner
	 * @param order
	 * @param nextStat	下一个状态／处理结果（52:已收到退货、核验中，53:核验不通过、协商解决，54:已重新发货、待收货，62：已收到退货、核验中，63:核验不通过、协商解决，64:同意退款，申请资金回退）
	 * @param content	评价内容
	 * @return {errcode,errmsg}
	 */
	public static JSONObject updAfterSales(PartnerBasic partner,Order order,
			String nextStat,String content) {
		JSONObject jsonRet = new JSONObject();
		Map<String,Object> params = new HashMap<String,Object>();
		//向服务中心发送申请
		String url = mfyxServerUrl + orderAfterSalesUrl;
		url = url.replace("{partnerId}", partner.getPartnerId() + "");
		url = url.replace("{orderId}", order.getOrderId() + "");
		params.put("nextStat", nextStat);
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
}
