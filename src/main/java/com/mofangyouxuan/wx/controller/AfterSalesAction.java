package com.mofangyouxuan.wx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.dto.Order;
import com.mofangyouxuan.dto.PartnerBasic;
import com.mofangyouxuan.dto.PayFlow;
import com.mofangyouxuan.dto.UserBasic;
import com.mofangyouxuan.service.OrderService;

@Controller
@RequestMapping("/aftersales")
@SessionAttributes({"userBasic","vipBasic","partnerBasic"})
public class AfterSalesAction {
	
	/**
	 * 获取用户售后管理页面
	 * @param map
	 * @return
	 */
	@RequestMapping("/user/mgr/{status}")
	public String getUserAfterSales(@PathVariable("status")String status,ModelMap map) {
		if(!"refund".equals(status) && !"exchange".equals(status)) {
			status = "refund";
		}
		map.put("status", status);
		map.put("sys_func", "user");
		return "aftersales/page-aftersales-user";
	}
	
	/**
	 * 获取商户售后管理页面
	 * @param map
	 * @return
	 */
	@RequestMapping("/partner/mgr")
	public String getMchtAfterSales(ModelMap map) {
		PartnerBasic partner = (PartnerBasic) map.get("partnerBasic");
		if(partner == null ) {
			map.put("errmsg", "您还未开通合作伙伴功能！");
		}
		return "aftersales/page-aftersales-mcht";
	}
	
	/**
	 * 获取用户退款页面
	 * @param orderId
	 * @param map
	 * @return
	 */
	@RequestMapping("/user/refund/begin/{orderId}")
	public String refundBegin(@PathVariable("orderId")String orderId,ModelMap map) {
		Order order = null;
		try {
			UserBasic user = (UserBasic)map.get("userBasic");
			JSONObject jsonRet = OrderService.getOrder(true, true, true, true, true, orderId);
			
			if(jsonRet != null && jsonRet.containsKey("order")) {
				order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"),Order.class);
				if(!user.getUserId().equals(order.getUserId())) {
					map.put("errmsg", "该订单不是您的宝贝订单！");
				}else {
					//startWith(order.status,'3') ||  startWith(order.status,'4') || order.status==='54' || order.status==='55' || order.status==='56')
					if(!order.getStatus().startsWith("3") && !order.getStatus().startsWith("4") &&
							!"54".equals(order.getStatus()) && !"55".equals(order.getStatus()) && !"56".equals(order.getStatus())) {
						map.put("errmsg", "您当前不可对订单进行申请退款！");
					}else {
						jsonRet = OrderService.getPayFlow(order, user.getUserId(), "1");
						if(jsonRet != null && jsonRet.containsKey("payFlow")) {
							PayFlow payFlow = JSONObject.toJavaObject(jsonRet.getJSONObject("payFlow"), PayFlow.class);
							map.put("payFlow", payFlow);
						}
						map.put("order", order);
					}
				}
			}else {
				map.put("errmsg", "获取订单信息失败！");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			map.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return "aftersales/page-aftersales-refund";
	}
	
	/**
	 * 买家提交申请退款
	 * 
	 * @param orderId
	 * @param type	退款类型：1-未收到或退款，3-签收退货；不可为空
	 * @param dispatchMode	退货的配送方式：1-官方统一配送、2-商家自行配送、3-快递配送、4-自取
	 * @param logisticsComp	退货的物流公司
	 * @param logisticsNo	退货的物流单号
	 * @param reason		退货缘由，不可为空
	 * @param map
	 * @return
	 */
	@RequestMapping("/user/refund/submit/{orderId}")
	@ResponseBody
	public String refundSubmit(@PathVariable("orderId")String orderId,
			@RequestParam(value="type",required=true)Integer type,
			Integer dispatchMode,String logisticsComp,String logisticsNo, 
			@RequestParam(value="reason",required=true)String reason,
			@RequestParam(value="passwd",required=false)String passwd,ModelMap map) {
		UserBasic user = (UserBasic) map.get("userBasic");
		JSONObject jsonRet = new JSONObject();
		try {
			//数据校验
			reason = reason.trim();
			if(type == null || (type != 1 && type != 3)) {
				jsonRet.put("errmsg", "退款类型不正确(1-未收到或退款，3-签收退货)！");
				jsonRet.put("errcode", ErrCodes.ORDER_PARAM_ERROR);
				return jsonRet.toString();
			}
			if(3 == type) { //退货
				if(null == dispatchMode || dispatchMode < 1 || dispatchMode > 4) {
					jsonRet.put("errmsg", "配送类型不正确(1-官方统一配送、2-商家自行配送、3-快递配送、4-自取)！");
					jsonRet.put("errcode", ErrCodes.ORDER_PARAM_ERROR);
					return jsonRet.toString();
				}
				if(logisticsComp == null || logisticsComp.length()<1) {
					jsonRet.put("errmsg", "配送方名称不可为空！");
					jsonRet.put("errcode", ErrCodes.ORDER_PARAM_ERROR);
					return jsonRet.toString();
				}
				if(logisticsNo == null || logisticsNo.length()<1) {
					jsonRet.put("errmsg", "物流单号不可为空！");
					jsonRet.put("errcode", ErrCodes.ORDER_PARAM_ERROR);
					return jsonRet.toString();
				}
			}
			if(reason == null || reason.length()<3) {
				jsonRet.put("errmsg", "退款理由不可少于3个字符！");
				jsonRet.put("errcode", ErrCodes.ORDER_PARAM_ERROR);
				return jsonRet.toString();
			}
			//数据与权限验证
			jsonRet = OrderService.getOrder(true, true, true, true, true, orderId);
			if(jsonRet == null || !jsonRet.containsKey("order")) {
				jsonRet.put("errmsg", "系统中没有该订单信息！");
				jsonRet.put("errcode", ErrCodes.ORDER_NO_EXISTS);
				return jsonRet.toString();
			}
			
			Order order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"), Order.class);
			if(!order.getUserId().equals(user.getUserId())) {
				jsonRet.put("errmsg", "您没有权限查询该订单信息！");
				jsonRet.put("errcode", ErrCodes.ORDER_PRIVILEGE_ERROR);
				return jsonRet.toString();
			}
			if(!order.getStatus().startsWith("3") && !order.getStatus().startsWith("4") &&
					!"54".equals(order.getStatus()) && !"55".equals(order.getStatus()) && !"56".equals(order.getStatus())) {
				jsonRet.put("errmsg", "您当前不可对订单进行申请退款！");
				jsonRet.put("errcode", ErrCodes.ORDER_PRIVILEGE_ERROR);
				return jsonRet.toString();
			}
			//发送请求
			JSONObject params = new JSONObject();
			params.put("reason", reason);
			if(3 == type) {
				params.put("dispatchMode", dispatchMode);
				params.put("logisticsComp", logisticsComp);
				params.put("logisticsNo", logisticsNo);
			}
			jsonRet = OrderService.applyRefund(user, order, type+"", params, passwd);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet.put("errcode",ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "出现系统错误！");
				return jsonRet.toString();
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode",ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
			jsonRet.toString();
		}
		return jsonRet.toString();
	}
	
	/**
	 * 获取用户换货申请页面
	 * @param orderId
	 * @param map
	 * @return
	 */
	@RequestMapping("/user/exchange/begin/{orderId}")
	public String exchangeBegin(@PathVariable("orderId")String orderId,ModelMap map) {
		Order order = null;
		try {
			UserBasic user = (UserBasic)map.get("userBasic");
			JSONObject jsonRet = OrderService.getOrder(true, true, true, true, true, orderId);
			
			if(jsonRet != null && jsonRet.containsKey("order")) {
				order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"),Order.class);
				if(!user.getUserId().equals(order.getUserId())) {
					map.put("errmsg", "该订单不是您的宝贝订单！");
				}else {
					if(!"40".equals(order.getStatus()) && !"41".equals(order.getStatus()) ) {
						map.put("errmsg", "您当前不可对该订单进行申请换货！");
					}else {
						map.put("order", order);
					}
				}
			}else {
				map.put("errmsg", "获取订单信息失败！");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			map.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return "aftersales/page-aftersales-exchange";
	}
	
	/**
	 * 买家提交申请换货
	 * @param orderId
	 * @param map
	 * @return
	 */
	@RequestMapping("/user/exchange/submit/{orderId}")
	@ResponseBody
	public String exchangeSubmit(@PathVariable("orderId")String orderId,
			Integer dispatchMode,String logisticsComp,String logisticsNo, 
			@RequestParam(value="reason",required=true)String reason,
			@RequestParam(value="passwd")String passwd,
			ModelMap map) {
		UserBasic user = (UserBasic) map.get("userBasic");
		JSONObject jsonRet = new JSONObject();
		try {
			
			if(null == dispatchMode || dispatchMode < 1 || dispatchMode > 4) {
				jsonRet.put("errmsg", "配送类型不正确(1-官方统一配送、2-商家自行配送、3-快递配送、4-自取)！");
				jsonRet.put("errcode", ErrCodes.ORDER_PARAM_ERROR);
				return jsonRet.toString();
			}
			if(logisticsComp == null || logisticsComp.length()<1) {
				jsonRet.put("errmsg", "配送方名称不可为空！");
				jsonRet.put("errcode", ErrCodes.ORDER_PARAM_ERROR);
				return jsonRet.toString();
			}
			if(logisticsNo == null || logisticsNo.length()<1) {
				jsonRet.put("errmsg", "物流单号不可为空！");
				jsonRet.put("errcode", ErrCodes.ORDER_PARAM_ERROR);
				return jsonRet.toString();
			}
			if(reason == null || reason.length()<3) {
				jsonRet.put("errmsg", "退款理由不可少于3个字符！");
				jsonRet.put("errcode", ErrCodes.ORDER_PARAM_ERROR);
				return jsonRet.toString();
			}
			
			jsonRet = OrderService.getOrder(true, true, true, true, true, orderId);
			if(jsonRet == null || !jsonRet.containsKey("order")) {
				jsonRet.put("errmsg", "系统中没有该订单信息！");
				jsonRet.put("errcode", ErrCodes.ORDER_NO_EXISTS);
				return jsonRet.toString();
			}
			
			Order order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"), Order.class);
			if(!order.getUserId().equals(user.getUserId())) {
				jsonRet.put("errmsg", "您没有权限查询该订单信息！");
				jsonRet.put("errcode", ErrCodes.ORDER_PRIVILEGE_ERROR);
				return jsonRet.toString();
			}
			if(!"40".equals(order.getStatus()) && !"41".equals(order.getStatus()) ) {
				jsonRet.put("errmsg", "您当前不可对订单进行申请换货！");
				jsonRet.put("errcode", ErrCodes.ORDER_PRIVILEGE_ERROR);
				return jsonRet.toString();
			}
			//发送请求
			JSONObject params = new JSONObject();
			params.put("reason", reason);
			params.put("dispatchMode", dispatchMode);
			params.put("logisticsComp", logisticsComp);
			params.put("logisticsNo", logisticsNo);
			jsonRet = OrderService.exchange(user, order, params);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet.put("errcode",ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "出现系统错误！");
				return jsonRet.toString();
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode",ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
			jsonRet.toString();
		}
		return jsonRet.toString();
	}
}
