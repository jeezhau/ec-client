package com.mofangyouxuan.wx.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.dto.Goods;
import com.mofangyouxuan.dto.GoodsSpec;
import com.mofangyouxuan.dto.Order;
import com.mofangyouxuan.dto.PartnerBasic;
import com.mofangyouxuan.dto.UserBasic;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.service.GoodsService;
import com.mofangyouxuan.service.OrderService;
import com.mofangyouxuan.service.UserVipService;
import com.mofangyouxuan.wx.utils.PageCond;

/**
 * 下单功能
 * 1、包含三部分：下单、支付、评价
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/order")
@SessionAttributes({"userBasic","vipBasic","partnerBasic"})
public class OrderAction {
	
	private String[] statusArr = new String[]{"all","4pay","4delivery","4sign","4appraise","4refund"};
	/**
	 * 选中商品开始下单
	 * @param goodsId
	 * @param map
	 * @return
	 */
	@RequestMapping("/place/{goodsId}")
	public String placeOrder(@PathVariable("goodsId")Long goodsId,ModelMap map) {
		//UserBasic user = (UserBasic) map.get("userBasic");
		Goods goods = null;
		JSONObject jsonRet = GoodsService.getGoods(true,goodsId);
		if(jsonRet == null || !jsonRet.containsKey("goods")) {
			map.put("errmsg", "获取商户信息失败！");
		}else {
			goods = JSONObject.toJavaObject(jsonRet.getJSONObject("goods"),Goods.class);
			map.put("goods", goods);
		}
		//
		return "order/page-place-order";
	}

	/**
	 * 买价完成下单信息，成功后跳转到开始支付
	 * 生成新点单，然后跳转至支付页面
	 * @param order
	 * @return
	 */
	@RequestMapping("/create")
	public String createOrder(@Valid Order order,BindingResult result,ModelMap map) {
		try {
			map.put("order", order);
			UserBasic user = (UserBasic) map.get("userBasic");
			
			Goods goods = null;
			JSONObject ret = GoodsService.getGoods(true,order.getGoodsId());
			if(ret == null || !ret.containsKey("goods")) {
				map.put("errmsg", "系统中没有该商户信息！");
				return "order/page-place-order";
			}else {
				goods = JSONObject.toJavaObject(ret.getJSONObject("goods"),Goods.class);
				map.put("goods", goods);
			}
			//信息验证结果处理
			if(result.hasErrors()){
				StringBuilder sb = new StringBuilder();
				List<ObjectError> list = result.getAllErrors();
				for(ObjectError e :list){
					sb.append(e.getDefaultMessage());
				}
				map.put("errmsg", sb.toString());
				return "order/page-place-order";
			}
			//购买信息检查
			List<GoodsSpec> specList = JSONArray.parseArray(order.getGoodsSpec(), GoodsSpec.class);
			if(specList == null || specList.size()<1) {
				map.put("errmsg", "购买信息格式有误！不可为空！");
				return "order/page-place-order";
			}
			for(int i=0; i<specList.size();i++) {
				GoodsSpec spec = specList.get(i);
				if(spec.getName() == null || spec.getName().trim().length()<1) {
					specList.remove(i);
				}
				if(spec.getBuyNum() == null || spec.getBuyNum()<1) {
					specList.remove(i);
				}
			}
			if(specList == null || specList.size()<1) {
				map.put("errmsg", "购买信息格式有误！不可为空！");
				return "order/page-place-order";
			}
			//数据处理
			order.setUserId(user.getUserId());
			JSONObject jsonRet = OrderService.createOrder(order, user.getUserId());
			if(jsonRet != null && jsonRet.containsKey("orderId"));{
				String orderId = jsonRet.getString("orderId");
				return "forward:/order/pay/begin/" + orderId; //跳转到支付页面
			}
		}catch(Exception e) {
			e.printStackTrace();
			map.put("errmsg", "出现异常，异常信息：" + e.getMessage());
			return "order/page-place-order";
		}
	}
	
	/**
	 * 获取支付页面
	 * @param orderId
	 * @param map
	 * @return
	 */
	@RequestMapping("/pay/begin/{orderId}")
	public String beiginPlay(@PathVariable("orderId")String orderId,HttpServletRequest request,ModelMap map) {
		Order order = null;
		try {
			UserBasic user = (UserBasic)map.get("userBasic");
			JSONObject jsonRet = OrderService.getOrder(true, null, null, null, true, orderId);
			
			if(jsonRet != null && jsonRet.containsKey("order")) {
				order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"),Order.class);
				if(!user.getUserId().equals(order.getUserId())) {
					map.put("errmsg", "该订单不是您的宝贝订单！");
				}else {
					if( "10".equals(order.getStatus()) || "12".equals(order.getStatus())) {//可支付
						map.put("order", order);
					}else {
						map.put("errmsg", "该订单当前不可再次支付！");
					}
				}
			}else {
				map.put("errmsg", "获取订单信息失败！");
			}
			//判断客户端是否支持微信支付
			String agent= request.getHeader("user-agent").toLowerCase();
			int index = agent.indexOf("micromessenger/");
			if(index >=0) {
				String sub = agent.substring(index + "micromessenger/".length());
				try {
					Double bb = Double.parseDouble(sub);
					if(bb > 5.0) {
						map.put("wxPay", "1");
					}
				}catch(Exception e) {
					
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			map.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return "order/page-pay-begin";
	}
	
	
	/**
	 * 买家选择支付渠道完成支付
	 * 修改订单状态，然后跳转至订单支付成功页面
	 * @param orderId
	 * @param status 支付结果 succees-成功，fail-失败
	 * @return
	 */
	@RequestMapping("/pay/finish/{orderId}/{status}")
	public String finishPay(@PathVariable("orderId")String orderId,
			@PathVariable("status")String status,ModelMap map) {
		Order order = null;
		try {
			UserBasic user = (UserBasic)map.get("userBasic");
			JSONObject jsonRet = OrderService.getOrder(true, null, null, null, true, orderId);
			
			if(jsonRet != null && jsonRet.containsKey("order")) {
				order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"),Order.class);
				if(!user.getUserId().equals(order.getUserId())) {
					map.put("errmsg", "该订单不是您的宝贝订单！");
				}else {//判断系统是否已经支付成功
					jsonRet = OrderService.payFinish(order, user, status);
					if(jsonRet != null && jsonRet.containsKey("errcode")) {
						map.put("payRetCode", jsonRet.getIntValue("errcode"));
						map.put("payRetMsg", jsonRet.getString("errmsg"));
						map.put("payType", jsonRet.getString("payType"));
						map.put("payTime", jsonRet.getString("payTime"));
						map.put("amount", jsonRet.getDouble("amount"));
						map.put("fee", jsonRet.getDouble("fee"));
						map.put("order", order);
					}else {
						map.put("errmsg", "获取支付结果信息失败！");
					}
				}
			}else {
				map.put("errmsg", "获取订单信息失败！");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			map.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return "order/page-pay-finished";
	}
	
	
	/**
	 * 买家订单显示界面获取
	 * @param status 订单状态:all(全部)、forPay（待付款）、forDlivery（待发货）、
	 * 				forTake（待收货）、forAppraise（待评价）、forRefund（待退款）
	 * @return
	 */
	@RequestMapping("/user/show/{status}")
	public String showAll4User(@PathVariable("status")String status,ModelMap map) {
		boolean flag = false;
		for(String s:statusArr) {
			if(s.equals(status)) {
				flag = true;
				break;
			}
		}
		if(!flag) {
			status = "all";
		}
		map.put("status", status);
		return "order/page-orders-show-user";
	}
	
	/**
	 * 买家订单显示界面获取
	 * @param status 订单状态:all(全部)、forPay（待付款）、forDlivery（待发货）、
	 * 				forTake（待收货）、forAppraise（待评价）、forRefund（待退款）
	 * @return
	 */
	@RequestMapping("/partner/show/{status}")
	public String showAll4Partner(@PathVariable("status")String status,ModelMap map) {
		boolean flag = false;
		for(String s:statusArr) {
			if(s.equals(status)) {
				flag = true;
				break;
			}
		}
		if(!flag) {
			status = "all";
		}
		map.put("status", status);
		map.put("sys_func", "partner-order");
		return "order/page-orders-show-partner";
	}
	
	/**
	 * 买家订单查询
	 * @param status
	 * @param map
	 * @return {errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
	 */
	@RequestMapping("/user/getall")
	@ResponseBody
	public String getAll4User(@RequestParam(required=true)String status,PageCond pageCond,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		UserBasic user = (UserBasic) map.get("userBasic");
		try {
			String statCode = "";
			boolean flag = false;
			for(String stat:statusArr) {
				if(stat.equals(status)) {
					flag = true;
					if("all".equals(stat)) {
						statCode = null;
					}else if("4pay".equals(stat)) {
						statCode = "10,12";
					}else if("4delivery".equals(stat)) {
						statCode = "20,21";
					}else if("4sign".equals(stat)) {
						statCode = "30";
					}else if("4appraise".equals(stat)) {
						statCode = "40";
					}else if("4refund".equals(stat)) {
						statCode = "50,51,52,53,54,55,60,61,62,63,64";
					}
					break;
				}
			}
			if(!flag) {
				statCode = null;
			}
			JSONObject params = new JSONObject();
			
			params.put("userId", user.getUserId());
			params.put("status", statCode);
			
			JSONObject sortParams = new JSONObject();
			sortParams.put("createTime", "1#1");
			
			JSONObject showGroups = new JSONObject();
			//needReceiver,needLogistics,needAppr,needAfterSales,needGoodsAndUser
			showGroups.put("needReceiver", true);
			showGroups.put("needLogistics", true);
			showGroups.put("needAppr", true);
			showGroups.put("needAfterSales", true);
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
	 * 买家订单查询
	 * @param status
	 * @param map
	 * @return {errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
	 */
	@RequestMapping("/partner/getall")
	@ResponseBody
	public String getAll4Partner(@RequestParam(required=true)String status,PageCond pageCond,
			ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		PartnerBasic partner = (PartnerBasic) map.get("partnerBasic");
		if(partner == null ) {
			jsonRet.put("errcode", ErrCodes.PARTNER_NO_EXISTS);
			jsonRet.put("errmsg", "您还未开通合作伙伴功能！");
			return jsonRet.toJSONString();
		}
		try {
			String statCode = "";
			boolean flag = false;
			for(String stat:statusArr) {
				if(stat.equals(status)) {
					flag = true;
					if("all".equals(stat)) {
						statCode = null;
					}else if("4pay".equals(stat)) {
						statCode = "10,12";
					}else if("4delivery".equals(stat)) {
						statCode = "20,21";
					}else if("4sign".equals(stat)) {
						statCode = "30";
					}else if("4appraise".equals(stat)) {
						statCode = "40";
					}else if("4refund".equals(stat)) {
						statCode = "50,51,52,53,54,55,60,61,62,63,64";
					}
					break;
				}
			}
			if(!flag) {
				statCode = null;
			}
			JSONObject params = new JSONObject();
			
			params.put("partnerId", partner.getPartnerId());
			params.put("status", statCode);
			
			JSONObject sortParams = new JSONObject();
			sortParams.put("createTime", "1#1");
			JSONObject showGroups = new JSONObject();
			//needReceiver,needLogistics,needAppr,needAfterSales,needGoodsAndUser
			showGroups.put("needReceiver", true);
			showGroups.put("needLogistics", true);
			showGroups.put("needAppr", true);
			showGroups.put("needAfterSales", true);
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
	 * 查询订单
	 * @param status
	 * @param orderId
	 * @return
	 */
	@RequestMapping("/search/{status}")
	@ResponseBody
	public String search(String status,String orderId,PageCond pageCond) {
		String[] arr = new String[]{"all","forPay","forDelivery","forTake","forAppraise","forRefund"};
		boolean flag = false;
		for(String s:arr) {
			if(s.equals(status)) {
				flag = true;
				break;
			}
		}
		if(!flag) {
			status = "all";
		}
		JSONObject jsonRet = new JSONObject();
		return jsonRet.toString();
	}
	
	/**
	 * 订单信息检查
	 * @param recvId		收货信息ID
	 * @param goodsId	购买商品ID
	 * @param goodsSpec	购买信息
	 * @param map
	 * @return {"errcode":0,"errmsg":"ok",match:[{postageId:'',mode:'',carrage:''}...]}
	 */
	@RequestMapping("/checkData")
	@ResponseBody
	public String checkOrderData(@RequestParam(value="recvId",required=true) Long recvId,
			@RequestParam(value="goodsId",required=true) Long goodsId,
			@RequestParam(value="goodsSpec",required=true) String goodsSpec,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			UserBasic user = (UserBasic) map.get("userBasic");
			//购买信息检查
			List<GoodsSpec> specList = JSONArray.parseArray(goodsSpec, GoodsSpec.class);
			if(specList == null || specList.size()<1) {
				jsonRet.put("errcode",ErrCodes.COMMON_PARAM_ERROR);
				jsonRet.put("errmsg", "购买信息格式有误！不可为空！");
				return jsonRet.toString();
			}
			for(int i=0; i<specList.size();i++) {
				GoodsSpec spec = specList.get(i);
				if(spec.getName() == null || spec.getName().trim().length()<1) {
					specList.remove(i);
				}
				if(spec.getBuyNum() == null || spec.getBuyNum()<1) {
					specList.remove(i);
				}
			}
			if(specList == null || specList.size()<1) {
				jsonRet.put("errcode",ErrCodes.COMMON_PARAM_ERROR);
				jsonRet.put("errmsg", "购买信息格式有误！不可为空！");
				return jsonRet.toString();
			}
			jsonRet = OrderService.checkOrderData(user.getUserId(), recvId, goodsId, JSONArray.toJSONString(specList));
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet.put("errcode",ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "出现系统错误！");
				return jsonRet.toString();
			}
			return jsonRet.toJSONString();
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode",ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
			jsonRet.toString();
		}
		return jsonRet.toString();
	}
	
	/**
	 * 获取用户的订单统计：按状态分组
	 * @param map
	 * @return
	 */
	@RequestMapping("/user/count")
	@ResponseBody
	public String getCounts4User(ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			UserBasic user = (UserBasic) map.get("userBasic");
			jsonRet = OrderService.countPartiByStatus(user.getUserId(), null, null);
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
	 * 显示订单详情：仅买卖双方可看到
	 * @param orderId
	 * @param map
	 */
	@RequestMapping("/detail/{orderId}")
	public String showDetail(@PathVariable("orderId")String orderId,ModelMap map) {
		
		UserBasic user = (UserBasic) map.get("userBasic");
		PartnerBasic partner= (PartnerBasic) map.get("partnerBasic");
		try {
			if(user == null && partner == null) {
				map.put("errmsg", "您没有权限查询该订单信息！");
			}else {
				JSONObject jsonRet = OrderService.getOrder(true, true, true, true, true, orderId);
				if(jsonRet == null || !jsonRet.containsKey("order")) {
					map.put("errmsg", "系统中没有该订单信息！");
				}else {
					Order order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"), Order.class);
					if(order.getUserId().equals(user.getUserId()) ||
							(partner != null && partner.getPartnerId().equals(order.getPartnerId()))) {
						map.put("order", order);
					}else {
						map.put("errmsg", "您没有权限查询该订单信息！");
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			map.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return "order/page-order-detail";
	}
	
	/**
	 * 取消自己的商家未备货的订单
	 * @param orderId
	 * @param map
	 * @return
	 */
	@RequestMapping("/cancel/{orderId}")
	@ResponseBody
	public String cancelorder(@PathVariable("orderId")String orderId,ModelMap map) {
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
			if(!"10".equals(order.getStatus()) && !"11".equals(order.getStatus()) && !"12".equals(order.getStatus()) && 
				!"20".equals(order.getStatus())) {
				jsonRet.put("errmsg", "您当前不可取消订单！");
				jsonRet.put("errcode", ErrCodes.ORDER_PRIVILEGE_ERROR);
				return jsonRet.toString();
			}
			//发送请求
			jsonRet = OrderService.cancelOrder(order, user);
			if(jsonRet == null || !jsonRet.containsKey("errocode")) {
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
	 * 用户选择支付方式后，根据支付方式生成付款单
	 * 1、如果是微信支付，则项微信支付发送预付单生成请求，成功返回后生成预付单；
	 * 2、余额支付则直接生成预付单；
	 * @param orderId
	 * @param payType 支付方式：1-余额，2-微信
	 * @param map
	 * @return {errcode,errmsg,payType,appId,timeStamp,nonceStr,prepay_id,paySign}
	 */
	@RequestMapping("/prepay/{orderId}/{payType}")
	@ResponseBody
	public String prepayOrder(@PathVariable("orderId")String orderId,
			@PathVariable("payType")Integer payType,HttpServletRequest request,
			ModelMap map) {
		UserBasic user = (UserBasic) map.get("userBasic");
		JSONObject jsonRet = new JSONObject();
		try {
			jsonRet = OrderService.getOrder(true, null, null, false, true, orderId);
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
			if(!"10".equals(order.getStatus()) && !"12".equals(order.getStatus())) {
				jsonRet.put("errmsg", "您当前不可再次申请支付订单！");
				jsonRet.put("errcode", ErrCodes.ORDER_PRIVILEGE_ERROR);
				return jsonRet.toString();
			}
			if(1 != payType && 2!= payType) {
				jsonRet.put("errcode", ErrCodes.ORDER_PARAM_ERROR);
				jsonRet.put("errmsg", "支付方式取值不正确！");
				return jsonRet.toJSONString();
			}
			if(1 == payType) {//更新会员余额信息
				VipBasic vipBasic = UserVipService.getVipBasic(user.getOpenId());
				if(vipBasic != null) {
					map.put("vipBasic", vipBasic);
				}
			}
			String ip = request.getRemoteHost();
			jsonRet = OrderService.prepayOrder(payType, order, user, ip);
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
	 * 
	 * @param orderId
	 * @return
	 */
	@RequestMapping("/partner/ready/{orderId}")
	public String readyOrUnGoods(@PathVariable(value="orderId")String orderId,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		PartnerBasic partner = (PartnerBasic) map.get("partnerBasic");
		if(partner == null ) {
			jsonRet.put("errcode", ErrCodes.PARTNER_NO_EXISTS);
			jsonRet.put("errmsg", "您还未开通合作伙伴功能！");
			return jsonRet.toJSONString();
		}
		try {
			jsonRet = OrderService.getOrder(true, null, null, null, true, orderId);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_DB_ERROR);
				jsonRet.put("errmsg", "获取订单信息失败！");
			}
			Order order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"), Order.class);
			if(!"20".equals(order.getStatus()) && !"21".equals(order.getStatus())) {
				jsonRet.put("errcode", ErrCodes.ORDER_PARAM_ERROR);
				jsonRet.put("errmsg", "该订单当前不可进行备货管理！");
				return jsonRet.toJSONString();
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	
}

