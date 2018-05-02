package com.mofangyouxuan.wx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.dto.Goods;
import com.mofangyouxuan.dto.UserBasic;
import com.mofangyouxuan.wx.utils.PageCond;

/**
 * 下单功能
 * 1、包含三部分：下单、支付、评价
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/order")
@SessionAttributes("userBasic")
public class OrderAction {
	
	/**
	 * 选中商品开始下单
	 * @param goodsId
	 * @param map
	 * @return
	 */
	@RequestMapping("/place/{goodsId}")
	public String placeOrder(@PathVariable("goodsId")String goodsId,ModelMap map) {
		UserBasic user = (UserBasic) map.get("userBasic");
		Goods goods = null;
		
		
		return "order/page-place-order";
	}
	
	/**
	 * 买价完成下单信息，并确认开始支付
	 * 生成新点单，然后跳转至支付页面
	 * @param order
	 * @return
	 */
	@RequestMapping("/pay/begin")
	public String beginPay(String jsonOrder) {
		
		
		return "order/page-pay-begin";
	}
	
	/**
	 * 买家选择支付渠道完成支付
	 * 修改订单状态，然后跳转至订单支付成功页面
	 * @param order
	 * @return
	 */
	@RequestMapping("/pay/finish")
	public String finishPay(String jsonOrder) {
		
		
		return "order/page-pay-finished";
	}
	
	/**
	 * 订单显示界面获取
	 * @param status 订单状态:all(全部)、forPay（待付款）、forDlivery（待发货）、
	 * 				forTake（待收货）、forAppraise（待评价）、forRefund（待退款）
	 * @return
	 */
	@RequestMapping("/show/{status}")
	public String showAll(@PathVariable("status")String status,ModelMap map) {
		String[] arr = new String[]{"all","forPay","forDlivery","forTake","forAppraise","forRefund"};
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
		map.put("status", status);
		return "order/page-order-show";
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
	
}
