package com.mofangyouxuan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.service.OrderService;
import com.mofangyouxuan.utils.PageCond;

/**
 * 评价管理
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/appraise")

public class AppraiseAction {

	/**
	 * 获取订单评价显示页面
	 * @param objId
	 * @param map
	 * @return
	 */
	@RequestMapping("/show/{objNm}/{objId}")
	public String showAll(@PathVariable("objNm")String objNm,@PathVariable("objId")Long objId,ModelMap map) {
		if(!"goods".equals(objNm) && "partnerId".equals(objNm)) {//访问非法
			objNm = null;
			objId = null;
		}
		map.put("objNm", objNm);
		map.put("objId", objId);
		return "order/page-appraise-show";
	}
	
	/**
	 * 分页显示指定商品的所有评价
	 * @param goodsId
	 * @param pageCond
	 * @param map
	 * @return
	 */
	@RequestMapping("/getall/goods/{goodsId}")
	@ResponseBody
	public Object getAllApprByGoods(@PathVariable("goodsId")Long goodsId,PageCond pageCond,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			JSONObject params = new JSONObject();
			
			params.put("goodsId", goodsId);
			params.put("status", "41,56");
			params.put("appraiseStatus", "1");
			
			JSONObject sortParams = new JSONObject();
			sortParams.put("createTime", "1#1");
			
			JSONObject showGroups = new JSONObject();
			//needReceiver,needLogistics,needAppr,needAfterSales,needGoodsAndUser
			//showGroups.put("needReceiver", true);
			//showGroups.put("needLogistics", true);
			showGroups.put("needAppr", true);
			//showGroups.put("needAfterSales", true);
			showGroups.put("needGoodsAndUser", true);
			
			jsonRet = OrderService.searchOrders(showGroups.toJSONString(),params.toJSONString(), sortParams.toString(), JSONObject.toJSONString(pageCond));
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_DB_ERROR);
				jsonRet.put("errmsg", "获取订单信息失败！");
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
	 * 分页显示指定合作伙伴得到的所有订单评价
	 * @param partnerId
	 * @param pageCond
	 * @param map
	 * @return
	 */
	@RequestMapping("/getall/partner/{partnerId}")
	@ResponseBody
	public Object getAllApprByPartner(@PathVariable("partnerId")Integer partnerId,PageCond pageCond,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			JSONObject params = new JSONObject();
			
			params.put("partnerId", partnerId);
			params.put("status", "41,56");
			params.put("appraiseStatus", "1");
			
			JSONObject sortParams = new JSONObject();
			sortParams.put("createTime", "1#1");
			
			JSONObject showGroups = new JSONObject();
			//needReceiver,needLogistics,needAppr,needAfterSales,needGoodsAndUser
			//showGroups.put("needReceiver", true);
			//showGroups.put("needLogistics", true);
			showGroups.put("needAppr", true);
			//showGroups.put("needAfterSales", true);
			showGroups.put("needGoodsAndUser", true);
			
			jsonRet = OrderService.searchOrders(showGroups.toJSONString(),params.toJSONString(), sortParams.toString(), JSONObject.toJSONString(pageCond));
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_DB_ERROR);
				jsonRet.put("errmsg", "获取订单信息失败！");
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
	 * 分页显示指定用户给出的所有订单评价
	 * @param userId
	 * @param pageCond
	 * @param map
	 * @return
	 */
	@RequestMapping("/getall/user/{userId}")
	@ResponseBody
	public Object getAllApprByUser(@PathVariable("userId")Integer userId,PageCond pageCond,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			JSONObject params = new JSONObject();
			
			params.put("userId", userId);
			params.put("status", "41,56");
			params.put("appraiseStatus", "1");
			
			JSONObject sortParams = new JSONObject();
			sortParams.put("createTime", "1#1");
			
			JSONObject showGroups = new JSONObject();
			//needReceiver,needLogistics,needAppr,needAfterSales,needGoodsAndUser
			//showGroups.put("needReceiver", true);
			//showGroups.put("needLogistics", true);
			showGroups.put("needAppr", true);
			//showGroups.put("needAfterSales", true);
			showGroups.put("needGoodsAndUser", true);
			
			jsonRet = OrderService.searchOrders(showGroups.toJSONString(),params.toJSONString(), sortParams.toString(), JSONObject.toJSONString(pageCond));
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_DB_ERROR);
				jsonRet.put("errmsg", "获取订单信息失败！");
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
