package com.mofangyouxuan.wx.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.dto.Goods;
import com.mofangyouxuan.dto.PartnerBasic;
import com.mofangyouxuan.dto.Postage;
import com.mofangyouxuan.dto.Receiver;
import com.mofangyouxuan.dto.UserBasic;
import com.mofangyouxuan.service.GoodsService;
import com.mofangyouxuan.service.ReceiverService;
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
	public String placeOrder(@PathVariable("goodsId")Long goodsId,ModelMap map) {
		UserBasic user = (UserBasic) map.get("userBasic");
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
	 * 获取是否可配送客户所选收货地区
	 * 1、如果不可配送则不可下单，并给出提示；
	 * 2、如果可以配送，则给出所有的可选模式以及对应的费用；
	 * @return {"errcode":0,"errmsg":"ok",match:[{postageId:'',mode:'',carrage:''}...]}
	 */
	@RequestMapping("/dispatchMatch")
	public String matchCanDispatch(@RequestParam(value="recvId",required=true) Long recvId,
			@RequestParam(value="goodsId",required=true) Integer goodsId,
			@RequestParam(value="partnerId",required=true) Integer partnerId,
			@RequestParam(value="weight",required=true) Integer weight,
			@RequestParam(value="amount",required=true) Double amount) {
		JSONObject jsonRet = new JSONObject();
		Goods goods = null;
		PartnerBasic partner = null;
		Receiver receiver = null;
		String postageIds = goods.getPostageIds();
		List<Postage> postages = null;
		
		String receiver_province = receiver.getProvince();
		String receiver_city = receiver.getCity(); //收货城市
		String partner_province = partner.getProvince();
		String partner_city = partner.getCity();	//商品所在城市
		
		Integer distance = null;
		if(receiver_city.equals(partner_city)) {//同城须计算距离，单位：km
			
		}
		JSONArray matchArray = new JSONArray();
		for(Postage postage:postages) {
			Double carrage = null;
			//同城配送
			if("1".equals(postage.getIsCityWide())) {
				Integer postage_distLimit = postage.getDistLimit(); //同城配送距离限制
				if(postage_distLimit == null) {
					postage_distLimit = 0;
				}
				//距离检查
				if(0 == postage_distLimit || postage_distLimit <= distance) {//可送
					//费送计算
					carrage = getCarrage(postage,distance,weight,amount);
				}else {//不可送
					continue;
				}
			}else {//全国配送
				String postage_provLimit = postage.getProvLimit(); //全国配送省份限制
				if(postage_provLimit == null) {
					postage_provLimit = "全国";
				}
				//省份检查
				if("全国".equals(postage_provLimit.trim()) || postage_provLimit.contains(receiver_province)){//可送
					carrage = getCarrage(postage,distance,weight,amount);	
				}else {//不可送
					continue;
				}
			}
			JSONObject match = new JSONObject();
			String[] modes = postage.getDispatchMode().split("");
			for(String mode:modes) {
				if(mode.trim().length()>0) {
					match.put("postageId", postage.getPostageId());
					match.put("mode", mode);
					match.put("carrage", new BigDecimal(carrage).setScale(2));
					matchArray.add(match);
				}
			}
		}
		if(matchArray.size()>0) {
			jsonRet.put("errcode", 0);
			jsonRet.put("errmsg", "ok");
			jsonRet.put("match", matchArray);
		}else {
			jsonRet.put("errcode", -1);
			jsonRet.put("errmsg", "该商品不支持该收件地区的配送！");
		}
		return jsonRet.toJSONString();
	}
	
	private double getCarrage(Postage postage,Integer distance,Integer weight,Double amount) {
		double carrage = 0.0;
		Integer freeWeight = postage.getFreeWeight() == null ? 1 : postage.getFreeWeight();
		Integer freeDist = postage.getFreeDist() == null ? 1 : postage.getFreeDist();
		Double freeAmount = postage.getFreeAmount() == null ? 100 : postage.getFreeAmount().doubleValue();
		if("1".equals(postage.getIsFree().trim())) {//无条件免邮
			return carrage;
		}else if("2".equals(postage.getIsFree().trim())) {//重量免邮
			if(weight <= freeWeight) {
				return carrage;
			}
		}else if("3".equals(postage.getIsFree().trim())) {//金额免邮
			if(amount >= freeAmount) {
				return carrage;
			}
		}else if("4".equals(postage.getIsFree().trim())) {//距离免邮
			if("1".equals(postage.getIsCityWide()) && distance <= freeDist) {
				return carrage;
			}
		}else if("23".equals(postage.getIsFree().trim())) {//重量+金额免邮
			if(weight <= freeWeight && amount >= freeAmount) {
				return carrage;
			}
		}else if("24".equals(postage.getIsFree().trim())) {//重量+距离免邮
			if(weight <= freeWeight && "1".equals(postage.getIsCityWide()) && distance <= freeDist) {
				return carrage;
			}
		}else if("34".equals(postage.getIsFree().trim())) {//金额+距离免邮
			if(amount >= freeAmount && "1".equals(postage.getIsCityWide()) && distance <= freeDist) {
				return carrage;
			}
		}else if("234".equals(postage.getIsFree().trim())) {//重量+金额+距离免邮
			if(weight <= freeWeight && amount >= freeAmount && "1".equals(postage.getIsCityWide()) && distance <= freeDist) {
				return carrage;
			}
		}
		
		Integer firstWeight = postage.getFirstWeight() == null ? 1 : postage.getFirstWeight();
		double firstWPrice = postage.getFirstWPrice() == null ? 10.0 : postage.getFirstWPrice().doubleValue();
		Integer additionWeight = postage.getAdditionWeight() == null ? 1 : postage.getAdditionWeight();
		double additonWPrice = postage.getAdditionWPrice() == null ? 5.0 : postage.getAdditionWPrice().doubleValue();
		if(firstWeight < 1) {
			firstWeight = 1;		//默认首重1kg
		}
		if(firstWPrice < 0) {
			firstWPrice = 10.0;;	//默认首重10元
		}
		if(additionWeight<1) {
			additionWeight = 1; //默认续重1kg
		}
		if(additonWPrice < 0) {
			additonWPrice = 5.0; //默认续重5元
		}
		carrage += firstWPrice;
		double cnt_w = Math.ceil((weight - firstWeight)/(additionWeight*1.0));
		if(cnt_w>0) {
			carrage += cnt_w * additonWPrice;
		}
		if("1".equals(postage.getIsCityWide())) {
			Integer firstDist = postage.getFirstDist() == null ? 1 : postage.getFirstDist();
			double firstDPrice = postage.getFirstDPrice() == null ? 3.0 : postage.getFirstDPrice().doubleValue();
			Integer additionDist = postage.getAdditionDist() == null ? 1 : postage.getAdditionDist();
			double additonDPrice = postage.getAdditionDPrice() == null ? 1.0 : postage.getAdditionDPrice().doubleValue();
			carrage += firstDPrice;
			double cnt_d = Math.ceil((distance - firstDist)/(additionDist*1.0));
			if(cnt_d>0) {
				carrage += cnt_d * additonDPrice;
			}
		}
		return carrage;
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
