package com.mofangyouxuan.controller.ums;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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
import com.mofangyouxuan.dto.Aftersale;
import com.mofangyouxuan.dto.Appraise;
import com.mofangyouxuan.dto.Goods;
import com.mofangyouxuan.dto.GoodsSpec;
import com.mofangyouxuan.dto.Order;
import com.mofangyouxuan.dto.PayFlow;
import com.mofangyouxuan.dto.UserBasic;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.service.AftersaleService;
import com.mofangyouxuan.service.AppraiseService;
import com.mofangyouxuan.service.GoodsService;
import com.mofangyouxuan.service.OrderService;
import com.mofangyouxuan.service.VipService;
import com.mofangyouxuan.utils.CommonUtil;
import com.mofangyouxuan.utils.PageCond;
import com.mofangyouxuan.utils.QRCodeUtil;

/**
 * 下单功能
 * 1、包含三部分：下单、支付、评价
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/order")
@SessionAttributes({"userBasic","vipBasic"})
public class OrderAction {
	
	private String[] statusArr = new String[]{"all","4pay","4delivery","4sign","4appraise","refund","exchange"};
	@Value("${sys.local-server-url}")
	private String localServerName;
	@Value("${sys.tmp-file-dir}")
	private String tmpFileDir;
	
	
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
					i--;
				}
				if(spec.getBuyNum() == null || spec.getBuyNum()<1) {
					specList.remove(i);
					i--;
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
	 * 选中商品开始下单，返回下单或加入购物车页面
	 * @param goodsId
	 * @param map
	 * @return
	 */
	@RequestMapping("/place/{goodsId}")
	public String placeOrder(@PathVariable("goodsId")Long goodsId,ModelMap map) {
		Goods goods = null;
		JSONObject jsonRet = GoodsService.getGoods(true,goodsId,false);
		if(jsonRet == null || !jsonRet.containsKey("goods")) {
			map.put("errmsg", "获取商户信息失败！");
		}else {
			goods = JSONObject.toJavaObject(jsonRet.getJSONObject("goods"),Goods.class);
			map.put("goods", goods);
		}
		
		return "order/page-place-order";
	}

	/**
	 * 完成下单信息
	 * 生成新点单，然后跳转至支付选择页面
	 * @param order
	 * @return
	 */
	@RequestMapping("/create")
	public String createOrder(@Valid Order order,BindingResult result,ModelMap map) {
		try {
			map.put("order", order);
			UserBasic user = (UserBasic) map.get("userBasic");
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
			order.setIncart("0");
			JSONObject jsonRet = OrderService.createOrder(order, user.getUserId());
			if(jsonRet != null && jsonRet.containsKey("orderId")){
				String orderId = jsonRet.getString("orderId");
				return "redirect:" + this.localServerName + "/order/pay/choose/" + orderId; //跳转到支付页面
			}else if(!jsonRet.containsKey("errcode")){
				map.put("errmsg", "订单生成失败，出现系统错误！");
			}else {
				map.put("errmsg", jsonRet.getString("errmsg"));
			}
		}catch(Exception e) {
			e.printStackTrace();
			map.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return "order/page-place-order";
	}
	
	/**
	 * 添加购物车
	 * 生成新点单，添加指购物车中
	 * @param order
	 * @return
	 */
	@RequestMapping("/cart/add")
	@ResponseBody
	public String addIncart(@Valid Order order,BindingResult result,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			UserBasic user = (UserBasic) map.get("userBasic");
			//信息验证结果处理
			if(result.hasErrors()){
				StringBuilder sb = new StringBuilder();
				List<ObjectError> list = result.getAllErrors();
				for(ObjectError e :list){
					sb.append(e.getDefaultMessage());
				}
				jsonRet.put("errcode",ErrCodes.COMMON_PARAM_ERROR);
				jsonRet.put("errmsg", "购买信息格式有误！不可为空！");
				return jsonRet.toJSONString();
			}
			//购买信息检查
			List<GoodsSpec> specList = JSONArray.parseArray(order.getGoodsSpec(), GoodsSpec.class);
			if(specList == null || specList.size()<1) {
				jsonRet.put("errcode",ErrCodes.COMMON_PARAM_ERROR);
				jsonRet.put("errmsg", "购买信息格式有误！不可为空！");
				return jsonRet.toJSONString();
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
				return jsonRet.toJSONString();
			}
			//数据处理
			order.setUserId(user.getUserId());
			order.setIncart("1");
			jsonRet = OrderService.createOrder(order, user.getUserId());
			if(jsonRet == null || !jsonRet.containsKey("errmsg")){
				jsonRet.put("errcode",-1);
				jsonRet.put("errmsg", "添加到购物车失败，出现系统错误！");
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode",ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toJSONString();
	}
	
	/**
	 * 修改订单
	 * @param order
	 * @return
	 */
	@RequestMapping("/update")
	@ResponseBody
	public String updateorder(@Valid Order order,BindingResult result,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			UserBasic user = (UserBasic) map.get("userBasic");
			//信息验证结果处理
			if(result.hasErrors()){
				StringBuilder sb = new StringBuilder();
				List<ObjectError> list = result.getAllErrors();
				for(ObjectError e :list){
					sb.append(e.getDefaultMessage());
				}
				jsonRet.put("errcode",ErrCodes.COMMON_PARAM_ERROR);
				jsonRet.put("errmsg", "购买信息格式有误！不可为空！");
				return jsonRet.toJSONString();
			}
			if(order.getOrderId() == null) {
				jsonRet.put("errcode",ErrCodes.COMMON_PARAM_ERROR);
				jsonRet.put("errmsg", "订单ID：不可为空！");
				return jsonRet.toJSONString();
			}
			//购买信息检查
			List<GoodsSpec> specList = JSONArray.parseArray(order.getGoodsSpec(), GoodsSpec.class);
			if(specList == null || specList.size()<1) {
				jsonRet.put("errcode",ErrCodes.COMMON_PARAM_ERROR);
				jsonRet.put("errmsg", "购买信息格式有误！不可为空！");
				return jsonRet.toJSONString();
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
				return jsonRet.toJSONString();
			}
			//数据处理
			order.setUserId(user.getUserId());
			jsonRet = OrderService.updateOrder(order, user.getUserId());
			if(jsonRet == null || !jsonRet.containsKey("errmsg")){
				jsonRet.put("errcode",-1);
				jsonRet.put("errmsg", "修改订单，出现系统错误！");
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode",ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toJSONString();
	}
	
	/**
	 * 删除购物车
	 * @param orderId
	 * @return
	 */
	@RequestMapping("/cart/remove/{orderId}")
	@ResponseBody
	public String removeIncart(@PathVariable("orderId")String orderId,
			String passwd,
			ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			UserBasic user = (UserBasic) map.get("userBasic");
			//数据处理
			jsonRet = OrderService.removeIncart(orderId, user.getUserId(), passwd);
			if(jsonRet == null || !jsonRet.containsKey("errmsg")){
				jsonRet.put("errcode",-1);
				jsonRet.put("errmsg", "删除购物车订单失败，出现系统错误！");
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode",ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toJSONString();
	}
	
	/**
	 * 获取支付工具选择页面
	 * @param orderId
	 * @param map
	 * @return
	 */
	@RequestMapping("/pay/choose/{orderId}")
	public String choosePlay(@PathVariable("orderId")String orderId,HttpServletRequest request,ModelMap map) {
		Order order = null;
		try {
			UserBasic user = (UserBasic)map.get("userBasic");
			JSONObject jsonRet = OrderService.getOrder( orderId);
			
			if(jsonRet != null && jsonRet.containsKey("order")) {
				order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"),Order.class);
				if(!user.getUserId().equals(order.getUserId())) {
					map.put("errmsg", "该订单不是您的宝贝订单！");
				}else {
					if( "10".equals(order.getStatus()) || "12".equals(order.getStatus())) {//可支付
						order.setSpecList(JSONArray.parseArray(order.getGoodsSpec(), GoodsSpec.class));
						List<Order> list = new ArrayList<Order>();
						list.add(order);
						map.put("list", list);
						map.put("amount", order.getAmount().doubleValue());
						map.put("orderIds", orderId);
					}else {
						map.put("errmsg", "该订单当前不可再次支付！");
					}
				}
			}else {
				map.put("errmsg", "获取订单信息失败！");
			}
		}catch(Exception e) {
			e.printStackTrace();
			map.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return "order/page-pay-choose";
	}
	
	/**
	 * 获取支付工具选择页面
	 * @param orderIds	多个使用_分隔
	 * @param map
	 * @return
	 */
	@RequestMapping("/pay/btchoose")
	public String batchChoosePlay(@RequestParam(value="orderIds",required=true)String orderIds,
			HttpServletRequest request,ModelMap map) {
		UserBasic user = (UserBasic) map.get("userBasic");
		Order order = null;
		try {
			Set<String> orderArr = new TreeSet<String>(); //排序集
			for(String s:orderIds.split("_")) {
				orderArr.add(s);
			}
			if(orderArr.size()>10) {
				map.put("errmsg", "您选择批量支付的订单数量超过上限！");
				return "order/page-pay-choose";
			}
			List<Order> list = new ArrayList<Order>();
			BigDecimal amount = new BigDecimal(0);
			String errids = "";
			String okids = "";
			for(String orderId:orderArr) {
				orderId = orderId.trim();
				if(orderId.length()>20) {
					JSONObject jsonRet = OrderService.getOrder(orderId);
					if(jsonRet != null && jsonRet.containsKey("order")) {
						order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"),Order.class);
						if( ("10".equals(order.getStatus()) || "12".equals(order.getStatus())) && user.getUserId().equals(order.getUserId())) {//可支付
							order.setSpecList(JSONArray.parseArray(order.getGoodsSpec(), GoodsSpec.class));
							list.add(order);
							amount = amount.add(order.getAmount());
							okids += "_" + orderId;
						}else {
							errids += "," + orderId;
						}
					}else {
						errids += "," + orderId;
					}
				}
			}
			if(errids.length()>0) {
				map.put("ermsg", "下列订单不存在或不可再次支付，订单ID列表：【" + errids.substring(1)+ "】");
			}
			if(list.size()>0) {
				map.put("list", list);
				map.put("amount", amount.doubleValue());
				map.put("orderIds", okids.substring(1));
			}
		}catch(Exception e) {
			e.printStackTrace();
			map.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return "order/page-pay-choose";
	}
	
	/**
	 * 用户选择支付方式后，根据支付方式生成付款单
	 * 1、如果是微信支付，则项微信支付发送预付单生成请求，成功返回后生成预付单；
	 * 2、余额支付则直接生成预付单；
	 * @param orderIds	多个ID使用_分隔
	 * @param payType 支付方式：1-余额，2-微信
	 * @param map
	 * @return {errcode,errmsg,payType,prepay_id,outPayUrl}
	 */
	@RequestMapping("/prepay/{payType}/{orderIds}")
	@ResponseBody
	public String createPrepay(@PathVariable(value="orderIds",required=true)String orderIds,
			@PathVariable("payType")Integer payType,HttpServletRequest request,
			HttpSession session,ModelMap map) {
		UserBasic user = (UserBasic) map.get("userBasic");
		JSONObject jsonRet = new JSONObject();
		try {
			Set<String> orderArr = new TreeSet<String>(); //排序集
			for(String s:orderIds.split("_")) {
				orderArr.add(s);
			}
			if(orderArr.size()>10) {
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				jsonRet.put("errmsg", "您选择批量支付的订单数量超过上限！");
				return jsonRet.toJSONString();
			}
			List<Order> list = new ArrayList<Order>();
			BigDecimal amount = new BigDecimal(0);
			String errids = "";
			String okids = "";
			for(String orderId:orderArr) {
				orderId = orderId.trim();
				if(orderId.length()>20) {
					jsonRet = OrderService.getOrder(orderId);
					if(jsonRet != null && jsonRet.containsKey("order")) {
						Order order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"),Order.class);
						if( ("10".equals(order.getStatus()) || "12".equals(order.getStatus())) && user.getUserId().equals(order.getUserId())) {//可支付
							order.setSpecList(JSONArray.parseArray(order.getGoodsSpec(), GoodsSpec.class));
							list.add(order);
							amount = amount.add(order.getAmount());
							okids += "_" + orderId;
						}else {
							errids += "," + orderId;
						}
					}else {
						errids += "," + orderId;
					}
				}
			}
			if(errids.length()>0) {
				jsonRet.put("ermsg", "下列订单不存在或不可再次支付，订单ID列表：【" + errids.substring(1)+ "】");
				jsonRet.put("errcode", ErrCodes.ORDER_PARAM_ERROR);
				return jsonRet.toJSONString();
			}
			if(1 != payType && 2 != payType && 3 != payType) {
				jsonRet.put("errcode", ErrCodes.ORDER_PARAM_ERROR);
				jsonRet.put("errmsg", "支付方式取值不正确！");
				return jsonRet.toJSONString();
			}
			if(1 == payType) {//更新会员余额信息
				VipBasic vipBasic = VipService.getVipBasic(user.getUserId());
				if(vipBasic != null) {
					map.put("vipBasic", vipBasic);
				}
			}else if(2 == payType) {
				String platform = CommonUtil.getPlatform(request);
				if("apk".equals(platform) || "ios".equals(platform)) {
					payType = 22; //H5支付
					String agent= request.getHeader("user-agent").toLowerCase();
					int index = agent.indexOf("micromessenger/");
					if(index >=0) {
						String sub = agent.substring(index + "micromessenger/".length(),index + "micromessenger/".length()+2);
						try {
							Double bb = Double.parseDouble(sub);
							if(bb >= 5.0 && user.getOpenId() != null && user.getOpenId().length()>10) {
								payType = 21; //公众号支付
							}
						}catch(Exception e) {
							
						}
					}
				}else {
					payType = 23; //扫码支付
				}
			}else if(3 == payType) {
				String platform = CommonUtil.getPlatform(request);
				if("apk".equals(platform) || "ios".equals(platform)) {
					payType = 31; //wap支付
				}else {
					payType = 32; //web支付
				}
			}
			String ip = request.getRemoteHost();
			ip = CommonUtil.getIpAddr(request);
			orderIds = okids.substring(1);//排序ID列表
			jsonRet = OrderService.createPay(payType, orderIds, user, ip);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet.put("errcode",ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "出现系统错误！");
				return jsonRet.toString();
			}else {
				if(jsonRet.containsKey("outPayUrl") && 22 == payType) { //微信H5支付
					String outPayUrl = jsonRet.getString("outPayUrl");
					String redirectUrl = this.localServerName + "/order/pay/finish/" + orderIds;
					redirectUrl = URLEncoder.encode(redirectUrl, "utf8");
					outPayUrl = outPayUrl + "&redirect_url=" + redirectUrl;
					jsonRet.put("outPayUrl", outPayUrl);
				}
				if(jsonRet.containsKey("AliPayForm") && (payType==31 || payType ==32)) {
					session.setAttribute("AliPayForm_" + orderIds, jsonRet.getString("AliPayForm"));
				}
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
	 * 使用微信扫码进行支付
	 * @param orderId
	 * @return
	 */
	@RequestMapping("/pay/use/wxqrcode/{orderIds}")
	public String useWxPay(@PathVariable(value="orderIds",required=true)String orderIds,ModelMap map) {
		Order order = null;
		try {
			UserBasic user = (UserBasic)map.get("userBasic");
			Set<String> orderArr = new TreeSet<String>();
			for(String s:orderIds.split("_")) {
				orderArr.add(s);
			}
			if(orderArr.size()>10) {
				map.put("errmsg", "您选择批量支付的订单数量超过上限！");
				return "order/page-pay-usewx";
			}
			List<Order> list = new ArrayList<Order>();
			BigDecimal amount = new BigDecimal(0);
			String errids = "";
			String okids = "";
			for(String orderId:orderArr) {
				orderId = orderId.trim();
				if(orderId.length()>20) {
					JSONObject jsonRet = OrderService.getOrder(orderId);
					if(jsonRet != null && jsonRet.containsKey("order")) {
						order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"),Order.class);
						if( ("10".equals(order.getStatus()) || "12".equals(order.getStatus())) && user.getUserId().equals(order.getUserId())) {//可支付
							order.setSpecList(JSONArray.parseArray(order.getGoodsSpec(), GoodsSpec.class));
							list.add(order);
							amount = amount.add(order.getAmount());
							okids += "_" + orderId;
						}else {
							errids += "," + orderId;
						}
					}else {
						errids += "," + orderId;
					}
				}
			}
			if(errids.length()>0) {
				map.put("ermsg", "下列订单不存在或不可再次支付，订单ID列表：【" + errids.substring(1)+ "】");
				return "order/page-pay-usewx";
			}
			orderIds = okids.substring(1);
			if(list.size()>0) {
				map.put("list", list);
				map.put("amount", amount.doubleValue());
				map.put("orderIds", orderIds);
			}
			//获取批量支付流水
			JSONObject jsonRet =  OrderService.getPayFlow(orderIds, user.getUserId(), "1");
			if(jsonRet == null || !jsonRet.containsKey("payFlow")) {
				map.put("errmsg", "该订单的支付流水获取失败！");
				return "order/page-pay-usewx";
			}
			PayFlow payFlow = JSONObject.toJavaObject(jsonRet.getJSONObject("payFlow"), PayFlow.class);
			map.put("payFlow", payFlow);
			map.put("orderIds", orderIds);
			return "order/page-pay-usewx";
		}catch(Exception e) {
			e.printStackTrace();
			map.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return "order/page-pay-usewx";
	}

	/**
	 * 显示微信扫码支付的二维码
	 * @param orderId
	 * @param out
	 * @param request
	 * @param response
	 * @param map
	 */
	@RequestMapping("/pay/show/wxqrcode/{orderIds}")
	public void showWXPayQrcode(@PathVariable(value="orderIds",required=true)String orderIds,
			OutputStream out,HttpServletRequest request,HttpServletResponse response,ModelMap map) {
		try {
			UserBasic user = (UserBasic)map.get("userBasic");
			Set<String> orderArr = new TreeSet<String>();
			for(String s:orderIds.split("_")) {
				orderArr.add(s);
			}
			JSONObject jsonRet = OrderService.getPayFlow(orderIds, user.getUserId(), "1"); //获取批量支付流水
			if(jsonRet == null || !jsonRet.containsKey("payFlow")) {
				return;
			}
			PayFlow payFlow = JSONObject.toJavaObject(jsonRet.getJSONObject("payFlow"), PayFlow.class);
			if(payFlow == null || !"23".equals(payFlow.getPayType()) || payFlow.getOutTradeUrl() == null) {
				return;
			}
			String outUrl = payFlow.getOutTradeUrl();
			//生成二维码
			Resource resource = new ClassPathResource("static/img/mfyx_logo.jpeg");
			File logoTempfile = new File(this.tmpFileDir,"mfyx_logo.jpeg");
			if(!logoTempfile.exists()) {
				FileUtils.copyInputStreamToFile(resource.getInputStream(), logoTempfile);
			}
			String filename = QRCodeUtil.encode(outUrl,logoTempfile.getAbsolutePath(),this.tmpFileDir, 710,false);
			File file = new File(this.tmpFileDir+filename);
			InputStream is = new FileInputStream(file);
			response.setContentType("image/*");
			response.addHeader("filename", filename);
			OutputStream os = response.getOutputStream(); 
			byte[] buff = new byte[1024];
			int len = 0;
			while((len=is.read(buff))>0) {
				os.write(buff, 0, len);
			}
			os.flush();
			os.close();
			is.close();
			file.delete();
		
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 使用支付宝表单进行支付
	 * @param orderId
	 * @return
	 */
	@RequestMapping("/pay/use/aliform/{orderIds}")
	public void useAliPay(@PathVariable(value="orderIds",required=true)String orderIds,
			HttpServletRequest request,HttpServletResponse response,ModelMap map,HttpSession session) {
		try {
			
			UserBasic user = (UserBasic) map.get("userBasic");
			Integer payType = null;
			String platform = CommonUtil.getPlatform(request);
			if("apk".equals(platform) || "ios".equals(platform)) {
				payType = 31; //wap支付
			}else {
				payType = 32; //web支付
			}
			Set<String> orderArr = new TreeSet<String>();
			for(String s:orderIds.split("_")) {
				orderArr.add(s);
			}
			if(orderArr.size()>10) {
				map.put("errmsg", "您选择批量支付的订单数量超过上限！");
				return ;
			}
			List<Order> list = new ArrayList<Order>();
			BigDecimal amount = new BigDecimal(0);
			String errids = "";
			String okids = "";
			for(String orderId:orderArr) {
				orderId = orderId.trim();
				if(orderId.length()>20) {
					JSONObject jsonRet = OrderService.getOrder(orderId);
					if(jsonRet != null && jsonRet.containsKey("order")) {
						Order order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"),Order.class);
						if( ("10".equals(order.getStatus()) || "12".equals(order.getStatus())) && user.getUserId().equals(order.getUserId())) {//可支付
							order.setSpecList(JSONArray.parseArray(order.getGoodsSpec(), GoodsSpec.class));
							list.add(order);
							amount = amount.add(order.getAmount());
							okids += "_" + orderId;
						}else {
							errids += "," + orderId;
						}
					}else {
						errids += "," + orderId;
					}
				}
			}
			if(errids.length()>0) {
				map.put("ermsg", "下列订单不存在或不可再次支付，订单ID列表：【" + errids.substring(1)+ "】");
				return ;
			}
			orderIds = okids.substring(1);
			String aliPayForm = (String) session.getAttribute("AliPayForm_" + orderIds);
			if(aliPayForm == null) {
				JSONObject jsonRet = OrderService.createPay(payType, orderIds, user, "111");
				if(jsonRet == null || !jsonRet.containsKey("AliPayForm")) {
					return;
				}
				aliPayForm = jsonRet.getString("AliPayForm");
			}
			response.setContentType("text/html;charset=utf8");
			PrintWriter pw = response.getWriter();
			pw.write(aliPayForm);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 使用余额进行支付
	 * @param orderIds
	 * @return
	 */
	@RequestMapping("/pay/use/bal/{orderId}")
	public String useBalPay(@PathVariable(value="orderIds",required=true)String orderIds,ModelMap map) {
		Order order = null;
		try {
			UserBasic user = (UserBasic)map.get("userBasic");
			VipBasic vip = (VipBasic) map.get("vipBasic");
			if(vip == null || !"1".equals(vip.getStatus())) {
				map.put("errmsg", "您还未开通会员，不可使用余额支付！");
				return "order/page-pay-usebal";
			}else if(vip.getPasswd() == null || vip.getPasswd().length()<10) {
				map.put("errmsg", "您还未设置会员操作密码，不可使用余额支付！");
				return "order/page-pay-usebal";
			}
			Set<String> orderArr = new TreeSet<String>();
			for(String s:orderIds.split("_")) {
				orderArr.add(s);
			}
			if(orderArr.size()>10) {
				map.put("errmsg", "您选择批量支付的订单数量超过上限！");
				return "order/page-pay-usewx";
			}
			List<Order> list = new ArrayList<Order>();
			BigDecimal amount = new BigDecimal(0);
			String errids = "";
			String okids = "";
			for(String orderId:orderArr) {
				orderId = orderId.trim();
				if(orderId.length()>20) {
					JSONObject jsonRet = OrderService.getOrder(orderId);
					if(jsonRet != null && jsonRet.containsKey("order")) {
						order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"),Order.class);
						if( ("10".equals(order.getStatus()) || "12".equals(order.getStatus())) && user.getUserId().equals(order.getUserId())) {//可支付
							order.setSpecList(JSONArray.parseArray(order.getGoodsSpec(), GoodsSpec.class));
							list.add(order);
							amount = amount.add(order.getAmount());
							okids += "_" + orderId;
						}else {
							errids += "," + orderId;
						}
					}else {
						errids += "," + orderId;
					}
				}
			}
			if(errids.length()>0) {
				map.put("ermsg", "下列订单不存在或不可再次支付，订单ID列表：【" + errids.substring(1)+ "】");
				return "order/page-pay-usewx";
			}
			if(list.size()>0) {
				map.put("list", list);
				map.put("amount", amount.doubleValue());
				map.put("orderIds", okids.substring(1));
			}
			orderIds = okids.substring(1);
			JSONObject jsonRet = OrderService.getPayFlow(orderIds, user.getUserId(), "1");
			if(jsonRet == null || !jsonRet.containsKey("payFlow")) {
				map.put("errmsg", "该订单的支付流水获取失败！");
				return "order/page-pay-usebal";
			}
			PayFlow payFlow = JSONObject.toJavaObject(jsonRet.getJSONObject("payFlow"), PayFlow.class);
			map.put("payFlow", payFlow);
			return "order/page-pay-usebal";
		}catch(Exception e) {
			e.printStackTrace();
			map.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return "order/page-pay-usebal";
	}
	
	/**
	 * 提交余额支付
	 * @param orderId
	 * @param map
	 * @return
	 */
	@RequestMapping("/pay/submit/bal/{orderIds}")
	@ResponseBody
	public String submitBalPay(@PathVariable(value="orderIds",required=true)String orderIds,
			@RequestParam(value="passwd",required=true)String passwd,
			ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			VipBasic vip = (VipBasic) map.get("vipBasic");
			if(vip == null || !"1".equals(vip.getStatus())) {
				jsonRet.put("errcode", -1);
				jsonRet.put("errmsg", "您还未开通会员，不可使用余额支付！");
				return jsonRet.toJSONString();
			}else if(vip.getPasswd() == null || vip.getPasswd().length()<10) {
				jsonRet.put("errcode", -1);
				jsonRet.put("errmsg", "您还未设置会员操作密码，不可使用余额支付！");
				return jsonRet.toJSONString();
			}
			Set<String> orderArr = new TreeSet<String>();
			for(String s:orderIds.split("_")) {
				orderArr.add(s);
			}
			if(orderArr.size()>10) {
				jsonRet.put("errmsg", "您选择批量支付的订单数量超过上限！");
				jsonRet.put("errcode", -1);
				return jsonRet.toJSONString();
			}
			List<Order> list = new ArrayList<Order>();
			BigDecimal amount = new BigDecimal(0);
			String errids = "";
			String okids = "";
			for(String orderId:orderArr) {
				orderId = orderId.trim();
				if(orderId.length()>20) {
					jsonRet = OrderService.getOrder(orderId);
					if(jsonRet != null && jsonRet.containsKey("order")) {
						Order order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"),Order.class);
						if( ("10".equals(order.getStatus()) || "12".equals(order.getStatus())) && vip.getVipId().equals(order.getUserId())) {//可支付
							order.setSpecList(JSONArray.parseArray(order.getGoodsSpec(), GoodsSpec.class));
							list.add(order);
							amount = amount.add(order.getAmount());
							okids += "_" + orderId;
						}else {
							errids += "," + orderId;
						}
					}else {
						errids += "," + orderId;
					}
				}
			}
			if(errids.length()>0) {
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("ermsg", "下列订单不存在或不可再次支付，订单ID列表：【" + errids.substring(1)+ "】");
				return jsonRet.toJSONString();
			}
			orderIds = okids.substring(1);
			jsonRet = OrderService.submitBalPay(orderIds, vip.getVipId(), passwd);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "支付提交失败！");
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	
	/**
	 * 买家选择支付渠道完成支付
	 * 修改订单状态，然后跳转至订单支付成功页面
	 * @param orderId
	 * @return
	 */
	@RequestMapping("/pay/finish/{orderIds}")
	public String finishPay(@PathVariable("orderIds")String orderIds,
			ModelMap map) {
		try {
			UserBasic user = (UserBasic)map.get("userBasic");
			Set<String> orderArr = new TreeSet<String>();
			for(String s:orderIds.split("_")) {
				orderArr.add(s);
			}
			if(orderArr.size()>10) {
				map.put("errmsg", "您选择批量支付的订单数量超过上限！");
				return "order/page-pay-usewx";
			}
			List<Order> list = new ArrayList<Order>();
			String errids = "";
			String okids = "";
			for(String orderId:orderArr) {
				orderId = orderId.trim();
				if(orderId.length()>20) {
					JSONObject jsonRet = OrderService.getOrder(orderId);
					if(jsonRet != null && jsonRet.containsKey("order")) {
						Order order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"),Order.class);
						if(user.getUserId().equals(order.getUserId())) {//可支付
							order.setSpecList(JSONArray.parseArray(order.getGoodsSpec(), GoodsSpec.class));
							list.add(order);
							okids += "_" + orderId;
						}else {
							errids += "," + orderId;
						}
					}else {
						errids += "," + orderId;
					}
				}
			}
			if(errids.length()>0) {
				map.put("ermsg", "您没有权限处理下列订单，订单ID列表：【" + errids.substring(1)+ "】");
				return "order/page-pay-finished";
			}
			orderIds = okids.substring(1);
			if(list.size()>0) {
				map.put("list", list);
				map.put("orderIds", orderIds);
			}
			//获取批量支付流水
			JSONObject jsonRet =  OrderService.getPayFlow(orderIds, user.getUserId(), null);
			if(jsonRet == null || !jsonRet.containsKey("payFlow")) {
				map.put("errmsg", "该订单的支付流水获取失败！");
				return "order/page-pay-finished";
			}
			PayFlow payFlow = JSONObject.toJavaObject(jsonRet.getJSONObject("payFlow"), PayFlow.class);
			map.put("payFlow", payFlow);
			jsonRet = OrderService.payFinish(orderIds, user);
			if(jsonRet != null && jsonRet.containsKey("errcode")) {
				map.put("payRetCode", jsonRet.getIntValue("errcode"));
				map.put("payRetMsg", jsonRet.getString("errmsg"));
			}else {
				map.put("errmsg", "获取支付结果信息失败！");
			}
		}catch(Exception e) {
			e.printStackTrace();
			map.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return "order/page-pay-finished";
	}
	
	/**
	 * 获取订单信息
	 * @param orderId
	 * @return errcode,amount,payType,payTime,fee,errmsg}
	 */
	@RequestMapping("/pay/status/{orderId}")
	@ResponseBody
	public String getPayStatus(@PathVariable("orderId")String orderId,
			ModelMap map) {
		Order order = null;
		JSONObject jsonRet = new JSONObject();
		try {
			UserBasic user = (UserBasic)map.get("userBasic");
			JSONObject jsonOrder = OrderService.getOrder(orderId);
			if(jsonOrder != null && jsonOrder.containsKey("order")) {
				order = JSONObject.toJavaObject(jsonOrder.getJSONObject("order"),Order.class);
				if(!user.getUserId().equals(order.getUserId())) {
					jsonRet.put("errcode", -1);
					jsonRet.put("errmsg", "该订单不是您的宝贝订单！");
				}else {//判断系统是否已经支付成功
					jsonRet = OrderService.payFinish(orderId, user);
				}
			}else {
				jsonRet.put("errcode", -1);
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
	 * 买家订单显示界面获取
	 * @param status 订单状态:all(全部)、4pay（待付款）、4dlivery（待发货）、
	 * 				4sign（待收货）、4appraise（待评价）、refund（退款）、exchange(换货)
	 * @return
	 */
	@RequestMapping("/show/{status}")
	public String showAll(@PathVariable("status")String status,ModelMap map) {
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
		return "order/page-order-show";
	}
	
	/**
	 * 获取购物车订单显示页面
	 * @return
	 */
	@RequestMapping("/cart/show")
	public String showCart(ModelMap map) {
		return "order/page-cart-show";
	}
	
	/**
	 * 买家订单查询
	 * @param status
	 * @param incart 是否在购物车中(0-否，1-是）
	 * @param map
	 * @return {errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
	 */
	@RequestMapping("/getall/{incart}")
	@ResponseBody
	public String getAll(@PathVariable(value="incart",required=true)String incart,
			String status,
			PageCond pageCond,ModelMap map) {
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
					}else if("4sign".equals(stat)) {	//待收货
						statCode = "30,54";
					}else if("4appraise".equals(stat)) { //待初次评价
						statCode = "40,55";
					}else if("refund".equals(stat)) { //可申请退款与已经进行中
						statCode = "20,21,22,30,31,40,41,50,51,52,53,54,55,56,60,61,62,63,64,65,67,DF";
					}else if("exchange".equals(stat)) { //可申请退货与已经进行中
						statCode = "40,41,50,51,52,53,54,55,56";
					}
					break;
				}
			}
			if(!flag) {
				statCode = null;
			}
			if(incart == null || (!"0".equals(incart) && !"1".equals(incart))) {
				incart = "0";
			}
			
			JSONObject params = new JSONObject();
			params.put("userId", user.getUserId());
			params.put("status", statCode);
			params.put("incart", incart);
			JSONObject sortParams = new JSONObject();
			sortParams.put("createTime", "1#1");
			
			JSONObject showGroups = new JSONObject();
			//needReceiver,needLogistics,needGoodsAndUser
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
		JSONObject jsonRet = new JSONObject();
		return jsonRet.toString();
	}
	
	/**
	 * 获取用户的订单统计：按状态分组
	 * @param map
	 * @return
	 */
	@RequestMapping("/count/bystatus")
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
	 * 获取用户的订单统计：按状态分组
	 * @param map
	 * @return
	 */
	@RequestMapping("/count/incart")
	@ResponseBody
	public String getCountsIncart(ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			UserBasic user = (UserBasic) map.get("userBasic");
			JSONObject params = new JSONObject();
			params.put("userId", user.getUserId());
			params.put("incart", "1");
			jsonRet = OrderService.countOrders(params.toJSONString());
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
		try {
			if(user == null || user.getUserId() == null) {
				map.put("errmsg", "您没有权限查询该订单信息！");
				return "";
			}
			JSONObject jsonRet = OrderService.getOrder(orderId);
			JSONObject jsonApprMcht = AppraiseService.getAppraise(orderId, "1");
			//JSONObject jsonApprUser = AppraiseService.getAppraise(orderId, "2");
			if(jsonRet == null || !jsonRet.containsKey("order")) {
				map.put("errmsg", "系统中没有该订单信息！");
				return "";
			}
			Order order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"), Order.class);
			JSONObject jsonaf = AftersaleService.getAftersale(orderId);
			if(order.getUserId().equals(user.getUserId())) {
				jsonRet = OrderService.getPayFlow(orderId, user.getUserId(), null);
				if(jsonRet != null && jsonRet.containsKey("payFlow")) {
					PayFlow payFlow = JSONObject.toJavaObject(jsonRet.getJSONObject("payFlow"), PayFlow.class);
					map.put("payFlow", payFlow);
				}
				if(jsonApprMcht != null && jsonApprMcht.containsKey("appraise")) {
					Appraise appraise = JSONObject.toJavaObject(jsonApprMcht.getJSONObject("appraise"), Appraise.class);
					map.put("appraise", appraise);
				}
				if(jsonaf != null && jsonaf.containsKey("aftersale")) {
					Aftersale aftersale = JSONObject.toJavaObject(jsonaf.getJSONObject("aftersale"), Aftersale.class);
					map.put("aftersale", aftersale);
				}
				map.put("order", order);
			}else {
				map.put("errmsg", "您没有权限查询该订单信息！");
			}			
		}catch(Exception e) {
			e.printStackTrace();
			map.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return "order/page-order-detail";
	}
	
	/**
	 * 获取订单取消页面
	 * @param orderId
	 * @param map
	 * @return
	 */
	@RequestMapping("/cancel/begin/{orderId}")
	public String beginCancel(@PathVariable("orderId")String orderId,ModelMap map) {
		Order order = null;
		try {
			UserBasic user = (UserBasic)map.get("userBasic");
			JSONObject jsonRet = OrderService.getOrder(orderId);
			
			if(jsonRet != null && jsonRet.containsKey("order")) {
				order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"),Order.class);
				if(!user.getUserId().equals(order.getUserId())) {
					map.put("errmsg", "该订单不是您的宝贝订单！");
				}else {
					if(!"10".equals(order.getStatus()) && !"11".equals(order.getStatus()) && !"12".equals(order.getStatus()) && 
						!"20".equals(order.getStatus()) && !"DF".equals(order.getStatus())) {
						map.put("errmsg", "您当前不可取消订单！");
					}else {
						jsonRet = OrderService.getPayFlow(orderId, user.getUserId(), "1");
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
		return "order/page-order-cancel";
	}
	
	/**
	 * 取消自己的商家未备货的订单
	 * @param orderId
	 * @param map
	 * @return
	 */
	@RequestMapping("/cancel/submit/{orderId}")
	@ResponseBody
	public String submitCancel(@PathVariable("orderId")String orderId,
			@RequestParam(value="reason",required=true)String reason,
			String passwd,
			ModelMap map) {
		UserBasic user = (UserBasic) map.get("userBasic");
		JSONObject jsonRet = new JSONObject();
		try {
			jsonRet = OrderService.getOrder( orderId);
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
				!"20".equals(order.getStatus()) && !"DF".equals(order.getStatus())) {
				jsonRet.put("errmsg", "您当前不可取消订单！");
				jsonRet.put("errcode", ErrCodes.ORDER_PRIVILEGE_ERROR);
				return jsonRet.toString();
			}
			//发送请求
			jsonRet = OrderService.cancelOrder(order, user,reason,passwd);
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
	 * 申请延长收货
	 * @param orderId
	 * @param map
	 * @return
	 */
	@RequestMapping("/prolong/{orderId}")
	@ResponseBody
	public String signProlong(@PathVariable("orderId")String orderId,
			String passwd,ModelMap map) {
		UserBasic user = (UserBasic) map.get("userBasic");
		JSONObject jsonRet = new JSONObject();
		try {
			jsonRet = OrderService.getOrder(orderId);
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
			if(!"22".equals(order.getStatus()) && !"30".equals(order.getStatus()) && !"55".equals(order.getStatus())) {
				jsonRet.put("errmsg", "您当前不可申请延长收货！");
				jsonRet.put("errcode", ErrCodes.ORDER_PRIVILEGE_ERROR);
				return jsonRet.toString();
			}
			//发送请求
			jsonRet = OrderService.signProlong(order, user, passwd);
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
	 * 显示物流详情：仅买卖双方可看到
	 * @param orderId
	 * @param map
	 */
	@RequestMapping("/logistics/{orderId}")
	public String showLogistics(@PathVariable("orderId")String orderId,ModelMap map) {
		UserBasic user = (UserBasic) map.get("userBasic");
		try {
			if(user == null || user.getUserId() == null) {
				map.put("errmsg", "您没有权限查询该订单信息！");
				return "order/page-order-logistics";
			}
			JSONObject jsonRet = OrderService.getLogistics(orderId);
			if(jsonRet == null || !jsonRet.containsKey("order")) {
				if(jsonRet.containsKey("errmsg")) {
					map.put("errmsg", jsonRet.getString("errmsg"));
				}else {
					map.put("errmsg", "系统中没有该订单信息！");
				}
				return "order/page-order-logistics";
			}
			Order order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"), Order.class);
			if(order.getUserId().equals(user.getUserId())) {
				map.put("order", order);
			}else {
				map.put("errmsg", "您没有权限查询该订单信息！");
			}			
		}catch(Exception e) {
			e.printStackTrace();
			map.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return "order/page-order-logistics";
	}
	
	/**
	 * 获取订单评价页面
	 * @param orderId
	 * @param map
	 * @return
	 */
	@RequestMapping("/appraise/begin/{orderId}")
	public String beginAppraise(@PathVariable("orderId")String orderId,ModelMap map) {
		Order order = null;
		try {
			UserBasic user = (UserBasic)map.get("userBasic");
			JSONObject jsonRet = OrderService.getOrder(orderId);
			JSONObject jsonRetAppr = AppraiseService.getAppraise(orderId, "1");
			if(jsonRet != null && jsonRet.containsKey("order")) {
				order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"),Order.class);
				if(!user.getUserId().equals(order.getUserId())) {
					map.put("errmsg", "该订单不是您的宝贝订单！");
				}else {
					if(!"30".equals(order.getStatus()) && !"31".equals(order.getStatus()) && 
							!"40".equals(order.getStatus()) && !"41".equals(order.getStatus()) && 
							!"54".equals(order.getStatus()) && !"55".equals(order.getStatus()) && !"56".equals(order.getStatus())) {
						map.put("errmsg", "您当前不可对订单进行评价！");
					}else {
						if(jsonRetAppr != null && jsonRetAppr.containsKey("appraise")) {
							Appraise appraise = JSONObject.toJavaObject(jsonRetAppr.getJSONObject("appraise"), Appraise.class);
							map.put("appraise", appraise);
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
		return "order/page-order-appraise";
	}
	
	/**
	 * 买家提交订单评价
	 * @param orderId
	 * @param map
	 * @return
	 */
	@RequestMapping("/appraise/submit/{orderId}")
	@ResponseBody
	public String submitAppraise(@PathVariable("orderId")String orderId,
			Integer scoreLogistics,Integer scoreMerchant,Integer scoreGoods, 
			String content,ModelMap map) {
		UserBasic user = (UserBasic) map.get("userBasic");
		JSONObject jsonRet = new JSONObject();
		try {
			jsonRet = OrderService.getOrder(orderId);
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
			if(!"30".equals(order.getStatus()) && !"31".equals(order.getStatus()) && 
					!"40".equals(order.getStatus()) && !"41".equals(order.getStatus()) && 
					!"54".equals(order.getStatus()) && !"55".equals(order.getStatus()) && !"56".equals(order.getStatus())) {
				jsonRet.put("errmsg", "您当前不可对该订单进行评价！");
				jsonRet.put("errcode", ErrCodes.ORDER_PRIVILEGE_ERROR);
				return jsonRet.toString();
			}
			//发送请求
			jsonRet = AppraiseService.appr2Mcht(user, order, scoreLogistics, scoreMerchant, scoreGoods, content);
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


