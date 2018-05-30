package com.mofangyouxuan.wx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.dto.UserBasic;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.service.ComplainService;
import com.mofangyouxuan.wx.utils.PageCond;

/**
 * 用户投诉管理
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/complain")
@SessionAttributes({"userBasic","vipBasic","partnerBasic"})
public class ComplainAction {
	
	/**
	 * 投诉管理主页
	 * @param map
	 * @return
	 */
	@RequestMapping("/manage/{mode}")
	public String getManage4Order(@PathVariable("mode")String mode,
			String oprFlag,Integer cplanId,ModelMap map) {
		if(!"order".equals(mode) && !"partner".equals(mode)) {
			map.put("errmsg", "系统访问参数不正确！");
			return "page-comlain-manage";
		}
		//修改或删除的参数
		map.put("oprFlag", oprFlag); //D-删除
		map.put("cplanId", cplanId);
		map.put("sys_func", "user");
		return "srvcenter/page-complain-manage";
	}
	
	
	/**
	 * 获取显示订单投诉的页面
	 * @return
	 */
	@RequestMapping("/order/show")
	public String showOrderComplain() {
		
		
		return "srvcenter/page-complain-show";
	}
	
	/**
	 * 保存用户的订单投诉信息
	 * 
	 * @return {errmsg,errcode}
	 */
	@RequestMapping(value="/order/save",method=RequestMethod.POST)
	@ResponseBody
	public Object saveOrderComplain(@RequestParam(value="orderId",required=true)String orderId,
			@RequestParam(value="content",required=true)String content,
			@RequestParam(value="cplanId",required=true)Integer cplanId,
			@RequestParam(value="phone",required=true)String phone,
			ModelMap map ) {
		JSONObject jsonRet = new JSONObject();
		UserBasic user = (UserBasic) map.get("userBasic");
		try {
			orderId = orderId.trim();
			if(orderId.length() != 30) {
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				jsonRet.put("errmsg", "订单ID不正确！");
				return jsonRet.toString();
			}
			content = content.trim();
			if(content.length()<10 || content.length()>1000) {
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				jsonRet.put("errmsg", "投诉内容长度为10-1000位字符！");
				return jsonRet.toString();
			}
			if(cplanId <0) {
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				jsonRet.put("errmsg", "投诉ID不正确！");
				return jsonRet.toString();
			}
			jsonRet = ComplainService.saveOrderComplain(user.getUserId(), cplanId, content, orderId, phone);
		}catch(Exception e) {
			//数据处理
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}

	/**
	 * 删除用户的订单投诉信息
	 * 
	 * @param cplanId
	 * @return
	 */
	@RequestMapping(value="/order/delete/{cplanId}",method=RequestMethod.POST)
	@ResponseBody
	public Object deleteOrderComplain(@SessionAttribute("userBasic") UserBasic user,
			@PathVariable(value="cplanId",required=true)Integer cplanId) {
		JSONObject jsonRet = new JSONObject();
		try {
			if(cplanId < 1) {
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				jsonRet.put("errmsg", "投诉ID不正确！");
				return jsonRet.toString();
			}
			jsonRet = ComplainService.deleteOrderComplain(user.getUserId(), cplanId);
		}catch(Exception e) {
			//数据处理
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	
	/**
	 * 保存用户的合作伙伴投诉信息
	 * 
	 * @return {errmsg,errcode}
	 */
	@RequestMapping(value="/partner/save",method=RequestMethod.POST)
	@ResponseBody
	public Object savePartnerComplain(@RequestParam(value="partnerId",required=true)Integer partnerId,
			@RequestParam(value="content",required=true)String content,
			@RequestParam(value="cplanId",required=true)Integer cplanId,
			@RequestParam(value="phone",required=true)String phone,
			ModelMap map ) {
		JSONObject jsonRet = new JSONObject();
		VipBasic vip = (VipBasic) map.get("vipBasic");
		try {
			if(partnerId < 1) {
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				jsonRet.put("errmsg", "订单ID不正确！");
				return jsonRet.toString();
			}
			content = content.trim();
			if(content.length()<10 || content.length()>1000) {
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				jsonRet.put("errmsg", "投诉内容长度为10-1000位字符！");
				return jsonRet.toString();
			}
			if(cplanId <0) {
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				jsonRet.put("errmsg", "投诉ID不正确！");
				return jsonRet.toString();
			}
			jsonRet = ComplainService.savePartnerComplain(vip.getVipId(), cplanId, content, partnerId, phone);
		}catch(Exception e) {
			//数据处理
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}

	/**
	 * 删除用户的合作伙伴投诉信息
	 * 
	 * @param cplanId
	 * @return
	 */
	@RequestMapping(value="/partner/delete/{cplanId}",method=RequestMethod.POST)
	@ResponseBody
	public Object deletePartnerComplain(@SessionAttribute("vipBasic") VipBasic vip,
			@PathVariable(value="cplanId",required=true)Integer cplanId) {
		JSONObject jsonRet = new JSONObject();
		try {
			if(cplanId < 1) {
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				jsonRet.put("errmsg", "投诉ID不正确！");
				return jsonRet.toString();
			}
			jsonRet = ComplainService.deletePartnerComplain(vip.getVipId(), cplanId);
		}catch(Exception e) {
			//数据处理
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	
	/**
	 * 获取用户的所有投诉信息
	 * 
	 * @param user		用户信息
	 * @param searchParams	查询参数{goodsId, partnerId,status,phone,beginCreateTime,endCreateTime,beginDealTime,endDealTime,beginRevisitTime,endRevisitTime}
	 * @param sortsParams	排序参数{createTime:"N#0/1",dealTime:"N#0/1",revisitTime:"N#0/1"}
	 * @param pageCond	分页信息{begin:, pageSize:}
	 * @return
	 */
	@RequestMapping(value="/order/getall",method=RequestMethod.POST)
	@ResponseBody
	public Object getUsersOrderAll(@SessionAttribute("userBasic") UserBasic user,
			String searchParams,String sortsParams,
			PageCond pageCond) {
		JSONObject jsonRet = new JSONObject();
		try {
			JSONObject search = JSONObject.parseObject(searchParams);
			if(search == null) {
				search = new JSONObject();
			}
			search.put("cpType", "1");
			JSONObject sorts = JSONObject.parseObject(sortsParams);
			if(sorts == null) {
				sorts = new JSONObject();
				sorts.put("createTime", "1#1");
			}
			jsonRet = ComplainService.getUsersAll(user.getUserId(), search,sorts, pageCond);
		}catch(Exception e) {
			//数据处理
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
}

