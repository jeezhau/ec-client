package com.mofangyouxuan.wx.controller;

import java.math.BigInteger;
import java.util.List;

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
import com.mofangyouxuan.service.GoodsService;
import com.mofangyouxuan.service.OrderService;
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
				BigInteger orderId = jsonRet.getBigInteger("orderId");
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
	public String beiginPlay(@PathVariable("orderId")BigInteger orderId,ModelMap map) {
		Order order = null;
		try {
			JSONObject jsonRet = OrderService.getOrder(orderId);
			
			if(jsonRet != null && jsonRet.containsKey("order")) {
				order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"),Order.class);
			}else {
				map.put("errmsg", "获取订单信息失败！");
			}
		}catch(Exception e) {
			e.printStackTrace();
			map.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		map.put("order", order);
		return "order/page-begin-pay";
	}
	
	
	/**
	 * 买家选择支付渠道完成支付
	 * 修改订单状态，然后跳转至订单支付成功页面
	 * @param orderId
	 * @return
	 */
	@RequestMapping("/pay/finish/{orderId}")
	public String finishPay(@PathVariable("orderId")BigInteger orderId,ModelMap map) {
		
		
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
		return "order/page-user-order-show";
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
		return "order/page-partner-order-show";
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
			
			jsonRet = OrderService.searchOrders(params.toJSONString(), sortParams.toString(), JSONObject.toJSONString(pageCond));
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
			
			jsonRet = OrderService.searchOrders(params.toJSONString(), sortParams.toString(), JSONObject.toJSONString(pageCond));
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
	public String showDetail(@PathVariable("orderId")BigInteger orderId,ModelMap map) {
		
		UserBasic user = (UserBasic) map.get("userBasic");
		PartnerBasic partner= (PartnerBasic) map.get("partnerBasic");
		try {
			if(user == null && partner == null) {
				map.put("errmsg", "您没有权限查询该订单信息！");
			}else {
				JSONObject jsonRet = OrderService.getOrder(orderId);
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
}
