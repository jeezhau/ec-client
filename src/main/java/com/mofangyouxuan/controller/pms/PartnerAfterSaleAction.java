package com.mofangyouxuan.controller.pms;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.dto.Aftersale;
import com.mofangyouxuan.dto.Order;
import com.mofangyouxuan.dto.PartnerBasic;
import com.mofangyouxuan.dto.PartnerStaff;
import com.mofangyouxuan.dto.PayFlow;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.service.AftersaleService;
import com.mofangyouxuan.service.OrderService;
import com.mofangyouxuan.utils.PageCond;

@Controller
@RequestMapping("/paftersale")
@SessionAttributes({"sys_func","partnerUserTP","partnerPasswd","partnerStaff","partnerBindVip","myPartner"})
public class PartnerAfterSaleAction {
	
	
	/**
	 * 获取商户售后管理页面
	 * @param map
	 * @return
	 */
	@RequestMapping("/manage/{status}")
	public String getManage(@PathVariable("status")String status,ModelMap map) {
		if(!"4refund".equals(status) && !"4exchange".equals(status) &&
				!"refunding".equals(status) && !"exchanging".equals(status)) {
			status = "refunding";
		}
		map.put("status", status);
		map.put("sys_func", "aftersales");
		PartnerBasic myPartner = (PartnerBasic) map.get("myPartner");
		if(myPartner == null || !("S".equals(myPartner.getStatus()) || "C".equals(myPartner.getStatus()))) {
			map.put("errmsg", "您还未开通合作伙伴或状态限制！");
			return "redirect:/partner/manage" ;
		}
		
		map.put("sys_func", "partner-aftersale");
		return "paftersale/page-paftersale-manage";
	}
	
	private String[] aftersalesStat = {"refunding","exchanging"}; //退款中、退货中
	
	/**
	 * 买家、卖家退换货订单查询
	 * @param status
	 * @param map
	 * @return {errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
	 */
	@RequestMapping("/getall/{status}")
	@ResponseBody
	public String getAll(@PathVariable(value="status", required=true)String status,
			PageCond pageCond,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			PartnerBasic myPartner = (PartnerBasic) map.get("myPartner");
			if(myPartner == null || !("S".equals(myPartner.getStatus()) || "C".equals(myPartner.getStatus())) ){
				jsonRet.put("errcode", ErrCodes.PARTNER_NO_EXISTS);
				jsonRet.put("errmsg", "您还未开通合作伙伴功能！");
				return jsonRet.toJSONString();
			}
			String statCode = "";
			boolean flag = false;
			for(String stat:aftersalesStat) {
				if(stat.equals(status)) {
					flag = true;
					if("refunding".equals(stat)) {
						statCode = "60,61,62,63,64,65,66,67,68";
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
			params.put("partnerId", myPartner.getPartnerId());
			params.put("status", statCode);
			//{createTime:"N#0/1",sendTime:"N#0/1",signTime:"N#0/1",appraiseTime:"N#0/1",aftersalesApplyTime:"N#0/1",aftersalesDealTime:"N#0/1"}
			JSONObject sortParams = new JSONObject();
			sortParams.put("aftersalesApplyTime", "1#1");
			sortParams.put("createTime", "2#1");
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
	 * 卖家处理用户退款页面
	 * @param orderId
	 * @param map
	 * @return
	 */
	@RequestMapping("/refund/begin/{orderId}")
	public String refundDealBegin(@PathVariable("orderId")String orderId,ModelMap map) {
		Order order = null;
		try {
			PartnerBasic myPartner = (PartnerBasic) map.get("myPartner");
			if(myPartner == null || !("S".equals(myPartner.getStatus()) || "C".equals(myPartner.getStatus())) ){
				map.put("errmsg", "您还未开通合作伙伴功能或状态限制！");
				return "paftersale/page-paftersale-refund-deal";
			}
			
			JSONObject jsonRet = OrderService.getOrder(orderId);
			if(jsonRet != null && jsonRet.containsKey("order")) {
				order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"),Order.class);
				if(!myPartner.getPartnerId().equals(order.getPartnerId())) {
					map.put("errmsg", "该订单不是您的销售订单！");
				}else {
					if(!order.getStatus().startsWith("60") && !order.getStatus().startsWith("62") &&
							!"63".equals(order.getStatus()) ) {
						map.put("errmsg", "您当前不可对该订单进行退款处理！");
					}else {
						jsonRet = OrderService.getPayFlow(orderId, order.getUserId(), "1");
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
		return "paftersale/page-paftersale-refund-deal";
	}
	
	/**
	 * 麦家处理用户申请退款
	 * 
	 * @param orderId
	 * @param result	处理结果：61:退款受理中(待退货)，62：已收到退货、核验中，63:核验不通过、协商解决，64:同意退款，申请资金回退
	 * @param reason		处理理由明细，不可为空
	 * @param map
	 * @return
	 */
	@RequestMapping("/refund/submit/{orderId}")
	@ResponseBody
	public String refundDealSubmit(@PathVariable("orderId")String orderId,
			@RequestParam(value="result",required=true)String result,
			@RequestParam(value="reason",required=true)String reason,
			@RequestParam(value="recvAddr",required=false)String recvAddr,
			@RequestParam(value="recvName",required=false)String recvName,
			@RequestParam(value="recvPhone",required=false)String recvPhone,
			ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			//数据校验
			reason = reason.trim();
			if(reason == null || reason.length()<3) {
				jsonRet.put("errmsg", "处理理由明细不可少于3个字符！");
				jsonRet.put("errcode", ErrCodes.ORDER_PARAM_ERROR);
				return jsonRet.toString();
			}
			if("61".equals(result)) {
				if(recvAddr == null || recvName == null || recvPhone == null) {
					jsonRet.put("errmsg", "退货地址：不可为空！");
					jsonRet.put("errcode", ErrCodes.ORDER_PARAM_ERROR);
					return jsonRet.toString();
				}
			}
			//数据检查与权限校验
			PartnerBasic myPartner = (PartnerBasic) map.get("myPartner");
			String partnerUserTP = (String) map.get("partnerUserTP");
			String partnerPasswd = (String) map.get("partnerPasswd");
			VipBasic vip = (VipBasic) map.get("partnerBindVip");
			PartnerStaff staff = (PartnerStaff) map.get("partnerStaff");
			if(myPartner == null || !("S".equals(myPartner.getStatus()) || "C".equals(myPartner.getStatus()))) {
				jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
				jsonRet.put("errmsg", "您还未开通合作伙伴或状态限制！");
				return jsonRet.toJSONString();
			}
			Integer updateOpr = null;
			if("bindVip".equals(partnerUserTP)) {
				if(vip == null || !"1".equals(vip.getStatus())) {
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "系统获取您的会员信息失败！");
					return jsonRet.toJSONString();
				}
				updateOpr = vip.getVipId();
			}else {
				if(staff == null || staff.getPartnerId() == null) {
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "系统获取您的员工信息失败！");
					return jsonRet.toJSONString();
				}
				updateOpr = staff.getUserId();
			}
			jsonRet = OrderService.getOrder( orderId);
			if(jsonRet == null || !jsonRet.containsKey("order")) {
				jsonRet.put("errmsg", "系统中没有该订单信息！");
				jsonRet.put("errcode", ErrCodes.ORDER_NO_EXISTS);
				return jsonRet.toString();
			}
			Order order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"), Order.class);
			if(!order.getPartnerId().equals(myPartner.getPartnerId())) {
				jsonRet.put("errmsg", "您没有权限查询该订单信息！");
				jsonRet.put("errcode", ErrCodes.ORDER_PRIVILEGE_ERROR);
				return jsonRet.toString();
			}
			
			if(!"60".equals(order.getStatus()) && !"62".equals(order.getStatus()) && !"63".equals(order.getStatus())) {
				jsonRet.put("errmsg", "您当前不可对该订单进行退款处理！");
				jsonRet.put("errcode", ErrCodes.ORDER_PRIVILEGE_ERROR);
				return jsonRet.toString();
			}
			if("60".equals(order.getStatus())) {
				if(!"61".equals(result) && !"65".equals(result) && !"68".equals(result)) {
					jsonRet.put("errmsg", "处理结果：取值不正确！");
					jsonRet.put("errcode", ErrCodes.ORDER_PARAM_ERROR);
					return jsonRet.toString();
				}
			}else if("62".equals(order.getStatus())) {
				if(!"63".equals(result) && !"64".equals(result) && !"65".equals(result)) {
					jsonRet.put("errmsg", "处理结果：取值不正确！");
					jsonRet.put("errcode", ErrCodes.ORDER_PARAM_ERROR);
					return jsonRet.toString();
				}
			}else if("63".equals(order.getStatus())) {
				if(!"64".equals(result) && !"65".equals(result)) {
					jsonRet.put("errmsg", "处理结果：取值不正确！");
					jsonRet.put("errcode", ErrCodes.ORDER_PARAM_ERROR);
					return jsonRet.toString();
				}
			}
			//发送请求
			JSONObject params = new JSONObject();
			params.put("reason", reason);
			if("61".equals(result)) {
				params.put("recvAddr", recvAddr);
				params.put("recvName", recvName);
				params.put("recvPhone", recvPhone);
			}
			jsonRet = AftersaleService.updAfterSales(myPartner, order, result+"", params,partnerPasswd,updateOpr);
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
	 * 处理用户换货申请页面
	 * @param orderId
	 * @param map
	 * @return
	 */
	@RequestMapping("/exchange/begin/{orderId}")
	public String exchangeDealBegin(@PathVariable("orderId")String orderId,ModelMap map) {
		Order order = null;
		try {
			PartnerBasic myPartner = (PartnerBasic) map.get("myPartner");
			if(myPartner == null || !("S".equals(myPartner.getStatus()) || "C".equals(myPartner.getStatus())) ){
				map.put("errmsg", "您还未开通合作伙伴功能或状态限制！");
				return "paftersale/page-paftersale-refund-deal";
			}
			
			JSONObject jsonRet = OrderService.getOrder(orderId);
			JSONObject jsonaf = AftersaleService.getAftersale(orderId);
			if(jsonRet != null && jsonRet.containsKey("order") && jsonaf != null && jsonaf.containsKey("aftersale")) {
				order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"),Order.class);
				if(!myPartner.getPartnerId().equals(order.getPartnerId())) {
					map.put("errmsg", "该订单不是您的销售订单！");
				}else {
					if(!order.getStatus().startsWith("50") && !order.getStatus().startsWith("52") &&
							!"53".equals(order.getStatus()) ) {
						map.put("errmsg", "您当前不可对该订单进行换货处理！");
					}else {
						jsonRet = OrderService.getPayFlow(orderId, order.getUserId(), "1");
						if(jsonRet != null && jsonRet.containsKey("payFlow")) {
							PayFlow payFlow = JSONObject.toJavaObject(jsonRet.getJSONObject("payFlow"), PayFlow.class);
							map.put("payFlow", payFlow);
						}
						Aftersale aftersale = JSONObject.toJavaObject(jsonaf.getJSONObject("aftersale"), Aftersale.class);
						map.put("aftersale", aftersale);
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
		return "paftersale/page-paftersale-exchange-deal";
	}
	
	/**
	 * 卖家处理买家提交申请换货
	 * @param orderId
	 * @param map
	 * @return
	 */
	@RequestMapping("/exchange/submit/{orderId}")
	@ResponseBody
	public String exchangeDealSubmit(@PathVariable("orderId")String orderId,
			@RequestParam(value="result",required=true)String result,
			@RequestParam(value="recvAddr",required=false)String recvAddr,
			@RequestParam(value="recvName",required=false)String recvName,
			@RequestParam(value="recvPhone",required=false)String recvPhone,
			@RequestParam(value="dispatchMode",required=false)Integer dispatchMode,
			@RequestParam(value="logisticsComp",required=false)String logisticsComp,
			@RequestParam(value="logisticsNo",required=false)String logisticsNo, 
			@RequestParam(value="reason",required=true)String reason,
			ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			//数据校验
			reason = reason.trim();
			if(reason == null || reason.length()<3) {
				jsonRet.put("errmsg", "处理理由明细：不可少于3个字符！");
				jsonRet.put("errcode", ErrCodes.ORDER_PARAM_ERROR);
				return jsonRet.toString();
			}
			if("51".equals(result)) {
				if(recvAddr == null || recvName == null || recvPhone == null) {
					jsonRet.put("errmsg", "退货地址：不可为空！");
					jsonRet.put("errcode", ErrCodes.ORDER_PARAM_ERROR);
					return jsonRet.toString();
				}
			}
			if("55".equals(result)) {
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
			//数据检查与权限校验
			PartnerBasic myPartner = (PartnerBasic) map.get("myPartner");
			String partnerUserTP = (String) map.get("partnerUserTP");
			String partnerPasswd = (String) map.get("partnerPasswd");
			VipBasic vip = (VipBasic) map.get("partnerBindVip");
			PartnerStaff staff = (PartnerStaff) map.get("partnerStaff");
			if(myPartner == null || !("S".equals(myPartner.getStatus()) || "C".equals(myPartner.getStatus()))) {
				jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
				jsonRet.put("errmsg", "您还未开通合作伙伴或状态限制！");
				return jsonRet.toJSONString();
			}
			Integer updateOpr = null;
			if("bindVip".equals(partnerUserTP)) {
				if(vip == null || !"1".equals(vip.getStatus())) {
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "系统获取您的会员信息失败！");
					return jsonRet.toJSONString();
				}
				updateOpr = vip.getVipId();
			}else {
				if(staff == null || staff.getPartnerId() == null) {
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "系统获取您的员工信息失败！");
					return jsonRet.toJSONString();
				}
				updateOpr = staff.getUserId();
			}
			jsonRet = OrderService.getOrder(orderId);
			if(jsonRet == null || !jsonRet.containsKey("order")) {
				jsonRet.put("errmsg", "系统中没有该订单信息！");
				jsonRet.put("errcode", ErrCodes.ORDER_NO_EXISTS);
				return jsonRet.toString();
			}
			
			Order order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"), Order.class);
			if(!order.getPartnerId().equals(myPartner.getPartnerId())) {
				jsonRet.put("errmsg", "您没有权限处理该订单！");
				jsonRet.put("errcode", ErrCodes.ORDER_PRIVILEGE_ERROR);
				return jsonRet.toString();
			}
			if(!"50".equals(order.getStatus()) && !"52".equals(order.getStatus()) && !"53".equals(order.getStatus())) {
				jsonRet.put("errmsg", "您当前不可对该订单进行换货处理！");
				jsonRet.put("errcode", ErrCodes.ORDER_PRIVILEGE_ERROR);
				return jsonRet.toString();
			}
			if("50".equals(order.getStatus())) {
				if(!"51".equals(result) && !"58".equals(result)) {
					jsonRet.put("errmsg", "处理结果：取值不正确！");
					jsonRet.put("errcode", ErrCodes.ORDER_PARAM_ERROR);
					return jsonRet.toString();
				}
			}else if("52".equals(order.getStatus())) {
				if(!"53".equals(result) && !"54".equals(result) && !"55".equals(result)) {
					jsonRet.put("errmsg", "处理结果：取值不正确！");
					jsonRet.put("errcode", ErrCodes.ORDER_PARAM_ERROR);
					return jsonRet.toString();
				}
			}else if("53".equals(order.getStatus())) {
				if(!"54".equals(result) && !"55".equals(result)) {
					jsonRet.put("errmsg", "处理结果：取值不正确！");
					jsonRet.put("errcode", ErrCodes.ORDER_PARAM_ERROR);
					return jsonRet.toString();
				}
			}
			//发送请求
			JSONObject params = new JSONObject();
			params.put("reason", reason);
			if("51".equals(result)) {
				params.put("recvAddr", recvAddr);
				params.put("recvName", recvName);
				params.put("recvPhone", recvPhone);
			}else if("55".equals(result)) {
				params.put("dispatchMode", dispatchMode);
				params.put("logisticsComp", logisticsComp);
				params.put("logisticsNo", logisticsNo);
			}
			jsonRet = AftersaleService.updAfterSales(myPartner, order, result+"", params,partnerPasswd,updateOpr);
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


