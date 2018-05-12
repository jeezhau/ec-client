package com.mofangyouxuan.service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.juli.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.dto.Order;
import com.mofangyouxuan.dto.UserBasic;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.wx.utils.HttpUtils;
import com.mofangyouxuan.wx.utils.NonceStrUtil;
import com.mofangyouxuan.wx.utils.ObjectToMap;
import com.mofangyouxuan.wx.utils.SignUtils;


/**
 * 订单管理服务
 * @author jeekhan
 *
 */
@Component
public class OrderService {
	private static Logger logger = LoggerFactory.getLogger(OrderService.class);
	private static String mfyxServerUrl;
	private static String orderCreateUrl;
	private static String orderUpdateUrl;
	private static String orderGetUrl;
	private static String orderCheckDataUrl;
	private static String orderSearchUrl;
	private static String orderCountPartiByStatus;
	private static String orderCancelUrl;
	private static String orderPrepayUrl;
	private static String mfyxWXMchtId;	//微信支付商户号
	private static String appId;			//微信APPID
	private static String notifyUrl;		//微信支付回调地址
	private static String KEY;			//微信支付商户密钥
	private static Double wxFeeRate;		//微信手续费费率

	
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
	
	/**
	 * 用户查询自己的消费流水
	 * @param order
	 * @param user
	 * @param flowType
	 * @return
	 */
	public static JSONObject getConsumeFlow(Order order,UserBasic user,String flowType) {
		
		return null;
	}
	
	/**
	 * 取消订单
	 * @param orderId
	 * @param userId
	 * @return {errcode:0,errmsg:"ok"}
	 */
	public static JSONObject cancelOrder(Order order,UserBasic user) {
		//需要退款
		if("11".equals(order.getStatus()) || "20".equals(order.getStatus())) {
			//获取消费成功流水
			
		}
		
		String url = mfyxServerUrl + orderCancelUrl;
		url = url.replace("userId", order.getUserId() + "");
		url = url.replace("orderId", order.getOrderId() + "");
		//
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
	 * 2、余额支付则直接生成预付单；
	 * @param payType 支付方式
	 * @param order
	 * @param userId
	 * @return {errcode:0,errmsg:"ok"}
	 */
	public static JSONObject prepayOrder(String payType,Order order,UserBasic user,VipBasic vip,String ip) {
		JSONObject jsonRet = new JSONObject();
		Map<String,Object> params = new HashMap<String,Object>();
		BigDecimal amount = order.getAmount().multiply(new BigDecimal(100)).setScale(0);
		BigDecimal fee = new BigDecimal(0);
		//向微信申请预付单
		if("2".equals(payType)) {
			fee = amount.multiply(new BigDecimal(wxFeeRate)).setScale(0, BigDecimal.ROUND_CEILING);
			Long total_amount = amount.longValue() + fee.longValue();
			JSONObject wxRet = wxPrePay(order,total_amount,user.getOpenId(),ip);
			if(wxRet == null) {
				jsonRet.put("errcode", -1);
				jsonRet.put("errmsg", "微信支付预付单生成失败！");
				return jsonRet;
			}else if(wxRet.containsKey("prepay_id")) {//成功
				params.put("payAccount",user.getOpenId());
				params.put("outTradeNo", wxRet.getString("prepay_id"));
			}else {//失败
				jsonRet.put("errcode", -1);
				jsonRet.put("errmsg", "微信支付预付单生成失败，" + wxRet.getString("err_code_des"));
				return jsonRet;
			}
		}else{
			if(vip.getBalance().compareTo(amount)<0) {
				jsonRet.put("errcode", -1);
				jsonRet.put("errmsg", "余额支付失败，余额不足！");
				return jsonRet;
			}else {
				params.put("payAccount",vip.getVipId());
			}
		}

		params.put("userId",user.getUserId());
		params.put("goodsId",order.getGoodsId());
		params.put("orderId",order.getOrderId());
		params.put("flowType", "1");
		params.put("payType", payType);
		params.put("currencyType","CNY");
		params.put("payAmount",amount.longValue());
		params.put("feeAmount",fee.longValue());
		
		//向服务中心发送申请
		String url = mfyxServerUrl + orderPrepayUrl;
		url = url.replace("userId", user.getUserId() + "");
		url = url.replace("orderId", order.getOrderId() + "");
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
	 * 微信预付单申请
	 * @param order
	 * @param totalAmount	总金额
	 * @param openId
	 * @param ip
	 * @return {prepay_id,code_url}
	 */
	private static JSONObject wxPrePay(Order order,Long totalAmount,String openId,String ip) {
		JSONObject jsonRet = new JSONObject();
		try {
			Map<String,String> params = new HashMap<String,String>();
			String nonceStr = NonceStrUtil.getNonceStr(20);
			params.put("appid", appId);
			params.put("mch_id", mfyxWXMchtId);
			params.put("device_info", "WEB");
			params.put("nonce_str", nonceStr);
			params.put("sign_type", "MD5");
			params.put("body", order.getPartnerBusiName() + "-" + order.getGoodsName());
			params.put("detail", "<![CDATA[{ \"goods_detail\":" + order.getGoodsSpec() + "]]>");
			//params.put("attach", "");//附加数据
			params.put("out_trade_no", "" + order.getOrderId());//商户订单号
			params.put("fee_type", "CNY");//标价币种
			params.put("total_fee", "" + totalAmount);//订单总金额，单位为分
			params.put("spbill_create_ip", ip);//APP和网页支付提交用户端ip
			params.put("time_start", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));//订单生成时间，格式为yyyyMMddHHmmss
			//params.put("time_expire", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));//订单失效时间，格式为yyyyMMddHHmmss
			//params.put("goods_tag", "");//订单优惠标记，使用代金券或立减优惠功能时需要的参数
			params.put("notify_url", notifyUrl);	//异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数
			params.put("trade_type", "JSAPI");	//交易类型:JSAPI-公众号支付,NATIVE-扫码支付,APP-APP支付
			params.put("product_id", order.getGoodsId()+"");
			//params.put("limit_pay", "");//上传此参数no_credit--可限制用户不能使用信用卡支付
			params.put("openid", openId);//用户标识
			//params.put("scene_info", "");////该字段用于上报场景信息，目前支持上报实际门店信息。该字段为JSON对象数据，对象格式为{"store_info":{"id": "门店ID","name": "名称","area_code": "编码","address": "地址" }}
			
			
			Set<String> paramKeySet = new TreeSet<String>(params.keySet());
			StringBuilder sb = new StringBuilder();
			for(String key:paramKeySet) {
				sb.append("&" + key + "=" + params.get(key));
			}
			String strings = sb.substring(1) + "&key=" + KEY;
			//获取签名
			String sign = SignUtils.encodeMD5Hex(strings).toUpperCase();
			params.put("sign", sign);
			
			Element root = DocumentHelper.createElement("xml");
			for(Map.Entry<String, String> entry:params.entrySet()) {
				root.addElement(entry.getKey()).addText(entry.getValue());
			}
			
			logger.info("微信申请预付单，发送请求：" + root.asXML());
			String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
			String strRet = HttpUtils.doPostTextSSL(url, root.asXML());
			logger.info("微信申请预付单，接口返回：" + strRet);
			
			//解析应答
			Document doc = DocumentHelper.parseText(strRet);
			Element xmlElement = doc.getRootElement();
			Map<String,String> retMap = new HashMap<String,String>();
			Node return_code = xmlElement.selectSingleNode("return_code");
			if(return_code != null) {
				retMap.put("return_code", return_code.getText());
			}
			Node return_msg = xmlElement.selectSingleNode("return_msg");
			if(return_msg != null) {
				retMap.put("return_msg", return_msg.getText());
			}
			Node appid = xmlElement.selectSingleNode("appid");
			if(appid != null) {
				retMap.put("appid", appid.getText());
			}
			Node mch_id = xmlElement.selectSingleNode("mch_id");
			if(mch_id != null) {
				retMap.put("mch_id", mch_id.getText());
			}
			Node device_info = xmlElement.selectSingleNode("device_info");
			if(device_info != null) {
				retMap.put("device_info", device_info.getText());
			}
			Node nonce_str = xmlElement.selectSingleNode("nonce_str");
			if(nonce_str != null) {
				retMap.put("nonce_str", nonce_str.getText());
			}
			Node signNode = xmlElement.selectSingleNode("sign");
	//		if(signNode != null) {
	//			retMap.put("sign", signNode.getText());
	//		}
			Node result_code = xmlElement.selectSingleNode("result_code");
			if(result_code != null) {
				retMap.put("result_code", result_code.getText());
			}
			Node err_code = xmlElement.selectSingleNode("err_code");
			if(err_code != null) {
				retMap.put("err_code", err_code.getText());
			}
			Node err_code_des = xmlElement.selectSingleNode("err_code_des");
			if(err_code_des != null) {
				retMap.put("err_code_des", err_code_des.getText());
			}
			Node trade_type = xmlElement.selectSingleNode("trade_type");
			if(trade_type != null) {
				retMap.put("trade_type", trade_type.getText());
			}
			Node prepay_id = xmlElement.selectSingleNode("prepay_id");
			if(prepay_id != null) {
				retMap.put("prepay_id", prepay_id.getText());
			}
			Node code_url = xmlElement.selectSingleNode("code_url");
			if(code_url != null) {
				retMap.put("code_url", code_url.getText());
			}
			jsonRet.put("err_code", retMap.get("err_code"));
			jsonRet.put("err_code_des", retMap.get("err_code_des"));
			if("SUCCESS".equals(return_code.getText())) {
				if("SUCCESS".equals(result_code.getText())) {
					Set<String> retKeySet = new TreeSet<String>(retMap.keySet());
					sb = new StringBuilder();
					for(String key:retKeySet) {
						sb.append("&" + key + "=" + params.get(key));
					}
					String strings2 = sb.substring(1) + "&key=" + KEY;
					//获取签名
					String sign2 = SignUtils.encodeMD5Hex(strings2).toUpperCase();
					if(sign2.equals(signNode.getText())) {
						jsonRet.put("prepay_id", retMap.get("prepay_id"));
						jsonRet.put("code_url", retMap.get("code_url"));
					}else {
						jsonRet.put("err_code", retMap.get("SIGNERROR"));
						jsonRet.put("err_code_des", retMap.get("签名验证比匹配！"));
						logger.info("微信申请预付单：签名验证失败！");
					}
				}
				return jsonRet;
			}
		}catch(Exception e) {
			e.printStackTrace();
			logger.info("微信申请预付单，产生异常：" + e.getMessage());
		}
		return null;
	}
	
}
