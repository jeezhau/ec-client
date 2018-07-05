package com.mofangyouxuan.controller.ums;

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
import com.mofangyouxuan.dto.PayFlow;
import com.mofangyouxuan.dto.UserBasic;
import com.mofangyouxuan.service.OrderService;
import com.mofangyouxuan.utils.PageCond;

@Controller
@RequestMapping("/aftersale")
@SessionAttributes({"userBasic","vipBasic"})
public class AfterSaleAction {
	
	/**
	 * 获取用户售后管理页面
	 * @param map
	 * @return
	 */
	@RequestMapping("/manage/{status}")
	public String getManage(@PathVariable("status")String status,ModelMap map) {
		if(!"4refund".equals(status) && !"4exchange".equals(status) &&
				!"refunding".equals(status) && !"exchangeing".equals(status)) {
			status = "refunding";
		}
		map.put("status", status);
		map.put("sys_func", "user");
		return "aftersale/page-aftersale-manage";
	}
	
	
	
	private String[] aftersalesStat = {"4refund","refunding","4exchange","exchanging"}; //可申请退款、退款中、可申请退货、退货中
	
	/**
	 * 买家退换货订单查询
	 * @param status
	 * @param map
	 * @return {errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
	 */
	@RequestMapping("/getall/{status}")
	@ResponseBody
	public String getAllOrder(@PathVariable(value="status", required=true)String status,
			PageCond pageCond,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		UserBasic user = (UserBasic) map.get("userBasic");
		
		try {
			String statCode = "";
			boolean flag = false;
			for(String stat:aftersalesStat) {
				if(stat.equals(status)) {
					flag = true;
					if("4refund".equals(stat)) {
						statCode = "20,21,22,30,31,40,41,55,56,57,58"; //可申请退款
					}else if("refunding".equals(stat)) {
						statCode = "60,61,62,63,64,65,66,67,68";
					}else if("4exchange".equals(stat)) {	 //可申请换货
						statCode = "31,40,41,";
					}else if("exchanging".equals(stat)) {
						statCode = "50,51,52,53,54,55,56,57,58";
					}
				}
			}
			if(!flag) {
				jsonRet.put("errcode", ErrCodes.ORDER_PARAM_ERROR);
				jsonRet.put("errmsg", "访问参数不正确！");
				return jsonRet.toJSONString();
			}
			JSONObject params = new JSONObject();
			params.put("userId", user.getUserId());
			params.put("status", statCode);
			//{createTime:"N#0/1",sendTime:"N#0/1",signTime:"N#0/1",appraiseTime:"N#0/1",aftersalesApplyTime:"N#0/1",aftersalesDealTime:"N#0/1"}
			JSONObject sortParams = new JSONObject();
			if("4refund".equals(status) || "4exchange".equals(status)) {
				sortParams.put("createTime", "1#1");
			}else {
				sortParams.put("aftersalesApplyTime", "1#1");
				sortParams.put("createTime", "2#1");
			}
			JSONObject showGroups = new JSONObject();
			showGroups.put("needReceiver", false);
			showGroups.put("needLogistics", false);
			showGroups.put("needAppr", false);
			showGroups.put("needAfterSales", false);
			showGroups.put("needGoodsAndUser", true);
			jsonRet = OrderService.searchOrders(showGroups.toJSONString(),params.toJSONString(), sortParams.toString(), JSONObject.toJSONString(pageCond));
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_DB_ERROR);
				jsonRet.put("errmsg", "获取订单信息失败！");
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	/**
	 * 获取用户退款申请页面
	 * @param orderId
	 * @param map
	 * @return
	 */
	@RequestMapping("/refund/apply/begin/{orderId}")
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
					if(!order.getStatus().startsWith("3") && !order.getStatus().startsWith("4") &&
							!"55".equals(order.getStatus()) && !"56".equals(order.getStatus()) && 
							!"57".equals(order.getStatus()) && !"58".equals(order.getStatus()) &&
							!"67".equals(order.getStatus()) && !"61".equals(order.getStatus()) ) {
						map.put("errmsg", "您当前不可对该订单进行申请退款！");
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
		return "aftersale/page-aftersale-refund";
	}

	
	/**
	 * 买家提交申请退款
	 * 1、申请退款后等待卖家处理回复；
	 * 2、卖家同意退款则等待买家发货；
	 * @param orderId
	 * @param type		退款类型：1-未收到或退款，3-签收退货；不可为空
	 * @param reason		退款缘由，不可为空
	 * @param dispatchMode	退货的配送方式：1-官方统一配送、2-商家自行配送、3-快递配送、4-自取
	 * @param logisticsComp	退货的物流公司
	 * @param logisticsNo	退货的物流单号
	 * @param map
	 * @return
	 */
	@RequestMapping("/refund/submit/{orderId}")
	@ResponseBody
	public String refundSubmit(@PathVariable("orderId")String orderId,
			@RequestParam(value="type",required=false)Integer type,
			@RequestParam(value="reason",required=false)String reason,
			@RequestParam(value="passwd",required=false)String passwd,
			Integer dispatchMode,String logisticsComp,String logisticsNo,
			ModelMap map) {
		UserBasic user = (UserBasic) map.get("userBasic");
		JSONObject jsonRet = new JSONObject();
		try {
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
					!"55".equals(order.getStatus()) && !"56".equals(order.getStatus()) && 
					!"57".equals(order.getStatus()) && !"58".equals(order.getStatus()) &&
					!"67".equals(order.getStatus()) && !"61".equals(order.getStatus())) {
				jsonRet.put("errmsg", "您当前不可对该订单进行申请退款！");
				jsonRet.put("errcode", ErrCodes.ORDER_PRIVILEGE_ERROR);
				return jsonRet.toString();
			}
			if("61".equals(order.getStatus())) { //同意退货
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
			//数据校验
			if(!"61".equals(order.getStatus()) && (type == null || (type != 1 && type != 3))) {
				jsonRet.put("errmsg", "退款类型：取值不正确(1-未收到或退款，3-签收退货)！");
				jsonRet.put("errcode", ErrCodes.ORDER_PARAM_ERROR);
				return jsonRet.toString();
			}
			if(!"61".equals(order.getStatus()) && (reason == null || reason.length()<3)) {
				jsonRet.put("errmsg", "退款理由：不可少于3个字符！");
				jsonRet.put("errcode", ErrCodes.ORDER_PARAM_ERROR);
				return jsonRet.toString();
			}
			
			//发送请求
			JSONObject params = new JSONObject();
			params.put("reason", reason);
			if("61".equals(order.getStatus())) { //同意退货
				params.put("dispatchMode", dispatchMode);
				params.put("logisticsComp", logisticsComp);
				params.put("logisticsNo", logisticsNo);
				type = 3;
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
	@RequestMapping("/exchange/begin/{orderId}")
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
					if(!"40".equals(order.getStatus()) && !"41".equals(order.getStatus()) &&
							!"51".equals(order.getStatus())) {
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
		return "aftersale/page-aftersale-exchange";
	}
	
	/**
	 * 买家提交申请换货
	 * 1、买家先提出申请换货；
	 * 2、卖家同意则买家发货；
	 * @param orderId
	 * @param map
	 * @return
	 */
	@RequestMapping("/exchange/submit/{orderId}")
	@ResponseBody
	public String exchangeSubmit(@PathVariable("orderId")String orderId,
			@RequestParam(value="reason",required=true)String reason,
			@RequestParam(value="passwd")String passwd,
			Integer dispatchMode,String logisticsComp,String logisticsNo,
			ModelMap map) {
		UserBasic user = (UserBasic) map.get("userBasic");
		JSONObject jsonRet = new JSONObject();
		try {
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
			if(!"40".equals(order.getStatus()) && !"41".equals(order.getStatus()) && !"51".equals(order.getStatus())) {
				jsonRet.put("errmsg", "您当前不可对订单进行申请换货！");
				jsonRet.put("errcode", ErrCodes.ORDER_PRIVILEGE_ERROR);
				return jsonRet.toString();
			}
			
			//发送请求
			JSONObject params = new JSONObject();
			if("51".equals(order.getStatus())) {
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
				params.put("dispatchMode", dispatchMode);
				params.put("logisticsComp", logisticsComp);
				params.put("logisticsNo", logisticsNo);
			}

			if(!"51".equals(order.getStatus()) && (reason == null || reason.length()<3)) {
				jsonRet.put("errmsg", "换货理由：不可少于3个字符！");
				jsonRet.put("errcode", ErrCodes.ORDER_PARAM_ERROR);
				return jsonRet.toString();
			}
			params.put("reason", reason);
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


