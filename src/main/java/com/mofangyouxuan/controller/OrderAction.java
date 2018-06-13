package com.mofangyouxuan.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;

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
import com.mofangyouxuan.dto.Goods;
import com.mofangyouxuan.dto.GoodsSpec;
import com.mofangyouxuan.dto.Order;
import com.mofangyouxuan.dto.PayFlow;
import com.mofangyouxuan.dto.UserBasic;
import com.mofangyouxuan.dto.VipBasic;
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
	 * 选中商品开始下单
	 * @param goodsId
	 * @param map
	 * @return
	 */
	@RequestMapping("/place/{goodsId}")
	public String placeOrder(@PathVariable("goodsId")Long goodsId,ModelMap map) {
		//UserBasic user = (UserBasic) map.get("userBasic");
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
			
			Goods goods = null;
			JSONObject ret = GoodsService.getGoods(true,order.getGoodsId(),false);
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
	 * @param orderId
	 * @param payType 支付方式：1-余额，2-微信
	 * @param map
	 * @return {errcode,errmsg,payType,prepay_id,outPayUrl}
	 */
	@RequestMapping("/prepay/{orderId}/{payType}")
	@ResponseBody
	public String createPrepay(@PathVariable("orderId")String orderId,
			@PathVariable("payType")Integer payType,HttpServletRequest request,
			HttpSession session,ModelMap map) {
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
			jsonRet = OrderService.createPay(payType, order, user, ip);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet.put("errcode",ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "出现系统错误！");
				return jsonRet.toString();
			}else {
				if(jsonRet.containsKey("outPayUrl") && 22 == payType) { //微信H5支付
					String outPayUrl = jsonRet.getString("outPayUrl");
					String redirectUrl = this.localServerName + "/order/pay/finish/" + orderId;
					redirectUrl = URLEncoder.encode(redirectUrl, "utf8");
					outPayUrl = outPayUrl + "&redirect_url=" + redirectUrl;
					jsonRet.put("outPayUrl", outPayUrl);
				}
				if(jsonRet.containsKey("AliPayForm") && (payType==31 || payType ==32)) {
					session.setAttribute("AliPayForm_" + order.getOrderId(), jsonRet.getString("AliPayForm"));
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
	 * 显示微信扫码支付的二维码
	 * @param orderId
	 * @param out
	 * @param request
	 * @param response
	 * @param map
	 */
	@RequestMapping("/pay/show/wxqrcode/{orderId}")
	public void showWXPayQrcode(@PathVariable(value="orderId",required=true)String orderId,
			OutputStream out,HttpServletRequest request,HttpServletResponse response,ModelMap map) {
		Order order = null;
		try {
			UserBasic user = (UserBasic)map.get("userBasic");
			JSONObject jsonRet = OrderService.getOrder(true, null, null, null, true, orderId);
			if(jsonRet == null || !jsonRet.containsKey("order")) {
				//response.sendRedirect("/show/all"); //重定向至订单显示
				return;
			}
			order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"),Order.class);
			if(!user.getUserId().equals(order.getUserId())) {
				//response.sendRedirect("/show/all"); //重定向至订单显示
				return;
			}
			if( !"10".equals(order.getStatus()) && !"12".equals(order.getStatus())) {//可支付
				return;
			}
			jsonRet = OrderService.getPayFlow(order, user.getUserId(), "1");
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
	 * 使用微信扫码进行支付
	 * @param orderId
	 * @return
	 */
	@RequestMapping("/pay/use/wxqrcode/{orderId}")
	public String useWxPay(@PathVariable(value="orderId",required=true)String orderId,ModelMap map) {
		Order order = null;
		try {
			UserBasic user = (UserBasic)map.get("userBasic");
			JSONObject jsonRet = OrderService.getOrder(true, null, null, null, true, orderId);
			if(jsonRet != null && jsonRet.containsKey("order")) {
				order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"),Order.class);
				if(!user.getUserId().equals(order.getUserId())) {
					map.put("errmsg", "该订单不是您的订单！");
					return "order/page-pay-usewx";
				}
				if( "10".equals(order.getStatus()) || "12".equals(order.getStatus())) {//可支付
					map.put("order", order);
					jsonRet = OrderService.getPayFlow(order, user.getUserId(), "1");
					if(jsonRet == null || !jsonRet.containsKey("payFlow")) {
						map.put("errmsg", "该订单的支付待支付流水获取失败！");
						return "order/page-pay-usewx";
					}
					PayFlow payFlow = JSONObject.toJavaObject(jsonRet.getJSONObject("payFlow"), PayFlow.class);
					map.put("payFlow", payFlow);
				}else {
					map.put("errmsg", "该订单当前不可再次支付！");
				}
				return "order/page-pay-usewx";
			}
			map.put("errmsg", "获取订单信息失败！");
		}catch(Exception e) {
			e.printStackTrace();
			map.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return "order/page-pay-usewx";
	}
	
	/**
	 * 使用支付宝表单进行支付
	 * @param orderId
	 * @return
	 */
	@RequestMapping("/pay/use/aliform/{orderId}")
	public void useAliPay(@PathVariable(value="orderId",required=true)String orderId,
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
			JSONObject jsonRet = OrderService.getOrder(true, null, null, false, true, orderId);
			if(jsonRet == null || !jsonRet.containsKey("order")) {
				return;
			}
			Order order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"), Order.class);
			if(!order.getUserId().equals(user.getUserId())) {
				return;
			}
			if(!"10".equals(order.getStatus()) && !"12".equals(order.getStatus())) {
				return;
			}
			String aliPayForm = (String) session.getAttribute("AliPayForm_" + orderId);
			if(aliPayForm == null) {
				jsonRet = OrderService.createPay(payType, order, user, "111");
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
	 * @param orderId
	 * @return
	 */
	@RequestMapping("/pay/use/bal/{orderId}")
	public String useBalPay(@PathVariable(value="orderId",required=true)String orderId,ModelMap map) {
		Order order = null;
		try {
			UserBasic user = (UserBasic)map.get("userBasic");
			VipBasic vip = (VipBasic) map.get("vipBasic");
			if(vip == null || !"1".equals(vip.getStatus())) {
				map.put("errmsg", "您还未开通会员，不可使用余额支付！");
			}else if(vip.getPasswd() == null || vip.getPasswd().length()<10) {
				map.put("errmsg", "您还未设置会员操作密码，不可使用余额支付！");
			}else {
				JSONObject jsonRet = OrderService.getOrder(true, null, null, null, true, orderId);
				if(jsonRet != null && jsonRet.containsKey("order")) {
					order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"),Order.class);
					if(!user.getUserId().equals(order.getUserId())) {
						map.put("errmsg", "该订单不是您的订单！");
						return "order/page-pay-usebal";
					}
					if( "10".equals(order.getStatus()) || "12".equals(order.getStatus())) {//可支付
						map.put("order", order);
						jsonRet = OrderService.getPayFlow(order, user.getUserId(), "1");
						if(jsonRet == null || !jsonRet.containsKey("payFlow")) {
							map.put("errmsg", "该订单的支付待支付流水获取失败！");
							return "order/page-pay-usebal";
						}
						PayFlow payFlow = JSONObject.toJavaObject(jsonRet.getJSONObject("payFlow"), PayFlow.class);
						map.put("payFlow", payFlow);
					}else {
						map.put("errmsg", "该订单当前不可再次支付！");
					}
					return "order/page-pay-usebal";
				}
				map.put("errmsg", "获取订单信息失败！");
			}
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
	@RequestMapping("/pay/submit/bal/{orderId}")
	@ResponseBody
	public String submitBalPay(@PathVariable(value="orderId",required=true)String orderId,
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
			
			jsonRet = OrderService.submitBalPay(orderId, vip.getVipId(), passwd);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_DB_ERROR);
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
	@RequestMapping("/pay/finish/{orderId}")
	public String finishPay(@PathVariable("orderId")String orderId,
			ModelMap map) {
		Order order = null;
		try {
			UserBasic user = (UserBasic)map.get("userBasic");
			JSONObject jsonRet = OrderService.getOrder(true, null, null, null, true, orderId);
			
			if(jsonRet != null && jsonRet.containsKey("order")) {
				order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"),Order.class);
				if(!user.getUserId().equals(order.getUserId())) {
					map.put("errmsg", "该订单不是您的宝贝订单！");
				}else {//判断系统是否已经支付成功
					jsonRet = OrderService.payFinish(order, user);
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
			JSONObject jsonOrder = OrderService.getOrder(true, null, null, null, true, orderId);
			if(jsonOrder != null && jsonOrder.containsKey("order")) {
				order = JSONObject.toJavaObject(jsonOrder.getJSONObject("order"),Order.class);
				if(!user.getUserId().equals(order.getUserId())) {
					jsonRet.put("errcode", -1);
					jsonRet.put("errmsg", "该订单不是您的宝贝订单！");
				}else {//判断系统是否已经支付成功
					jsonRet = OrderService.payFinish(order, user);
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
	 * @param status 订单状态:all(全部)、forPay（待付款）、forDlivery（待发货）、
	 * 				forTake（待收货）、forAppraise（待评价）、forRefund（待退款）
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
	 * 买家订单查询
	 * @param status
	 * @param map
	 * @return {errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
	 */
	@RequestMapping("/getall")
	@ResponseBody
	public String getAll(@RequestParam(required=true)String status,PageCond pageCond,ModelMap map) {
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
						statCode = "20,21,22,30,31,40,41,50,51,52,53,54,55,56,60,61,62,63,64,65";
					}else if("exchange".equals(stat)) { //可申请退货与已经进行中
						statCode = "40,41,50,51,52,53,54,55,56";
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
	 * 获取用户的订单统计：按状态分组
	 * @param map
	 * @return
	 */
	@RequestMapping("/count")
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
		try {
			if(user == null || user.getUserId() == null) {
				map.put("errmsg", "您没有权限查询该订单信息！");
				return "";
			}
			JSONObject jsonRet = OrderService.getOrder(true, true, true, true, true, orderId);
			if(jsonRet == null || !jsonRet.containsKey("order")) {
				map.put("errmsg", "系统中没有该订单信息！");
				return "";
			}
			Order order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"), Order.class);
			if(order.getUserId().equals(user.getUserId())) {
				jsonRet = OrderService.getPayFlow(order, user.getUserId(), null);
				if(jsonRet != null && jsonRet.containsKey("payFlow")) {
					PayFlow payFlow = JSONObject.toJavaObject(jsonRet.getJSONObject("payFlow"), PayFlow.class);
					map.put("payFlow", payFlow);
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
			JSONObject jsonRet = OrderService.getOrder(true, null, null, null, true, orderId);
			
			if(jsonRet != null && jsonRet.containsKey("order")) {
				order = JSONObject.toJavaObject(jsonRet.getJSONObject("order"),Order.class);
				if(!user.getUserId().equals(order.getUserId())) {
					map.put("errmsg", "该订单不是您的宝贝订单！");
				}else {
					if(!"10".equals(order.getStatus()) && !"11".equals(order.getStatus()) && !"12".equals(order.getStatus()) && 
						!"20".equals(order.getStatus())) {
						map.put("errmsg", "您当前不可取消订单！");
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
			JSONObject jsonRet = OrderService.getOrder(true, true, true, true, true, orderId);
			
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
//						jsonRet = OrderService.getPayFlow(order, user.getUserId(), "1");
//						if(jsonRet != null && jsonRet.containsKey("payFlow")) {
//							PayFlow payFlow = JSONObject.toJavaObject(jsonRet.getJSONObject("payFlow"), PayFlow.class);
//							map.put("payFlow", payFlow);
//						}
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
			if(!"30".equals(order.getStatus()) && !"31".equals(order.getStatus()) && 
					!"40".equals(order.getStatus()) && !"41".equals(order.getStatus()) && 
					!"54".equals(order.getStatus()) && !"55".equals(order.getStatus()) && !"56".equals(order.getStatus())) {
				jsonRet.put("errmsg", "您当前不可对该订单进行评价！");
				jsonRet.put("errcode", ErrCodes.ORDER_PRIVILEGE_ERROR);
				return jsonRet.toString();
			}
			//发送请求
			jsonRet = OrderService.appr2Mcht(user, order, scoreLogistics, scoreMerchant, scoreGoods, content);
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


