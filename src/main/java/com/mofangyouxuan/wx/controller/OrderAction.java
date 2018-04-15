package com.mofangyouxuan.wx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 下单功能
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/order")
public class OrderAction {
	
	/**
	 * 选中商品开始下单
	 * @param goodsId
	 * @param map
	 * @return
	 */
	@RequestMapping("/order/begin/{goodsId}")
	public String beginOrder(@PathVariable("goodsId")String goodsId,ModelMap map) {
		
		return "page-order-begin";
	}
	
	/**
	 * 买价完成下单信息，并确认开始支付
	 * 生成新点单，然后跳转至支付页面
	 * @param order
	 * @return
	 */
	@RequestMapping("/pay/begin")
	public String beginPay(String jsonOrder) {
		
		
		return "page-pay-begin";
	}
	
	/**
	 * 买家选择支付渠道完成支付
	 * 修改订单状态，然后跳转至订单支付成功页面
	 * @param order
	 * @return
	 */
	@RequestMapping("/pay/finish")
	public String finishPay(String jsonOrder) {
		
		
		return "page-pay-finished";
	}
	
	
}
