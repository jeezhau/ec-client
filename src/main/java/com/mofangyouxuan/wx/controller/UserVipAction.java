package com.mofangyouxuan.wx.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.dto.UserBasic;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.service.UserVipService;
import com.mofangyouxuan.wx.utils.PageCond;

/**
 * 个人中心管理
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/user")
@SessionAttributes({"openId","vipBasic","userBasic","isDayFresh","sys_func"})
public class UserVipAction {
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	/**
	 * 用户管理主页面-我之基本
	 * @return
	 * @throws JSONException 
	 */
	@RequestMapping("/index/{mode}")
	public String getIndex(@PathVariable("mode")String mode,ModelMap map){
		//UserBasic user = (UserBasic) map.get("userBasic");
		if(!"basic".equals(mode) && !"vip".equals(mode)) {
			mode = "basic";
		}
		//重新获取用户基本信息，会员信息
		String openId = (String)map.get("openId");
		UserBasic userBasic = UserVipService.getUserBasic(openId);
		if(userBasic != null) {
			map.put("userBasic", userBasic);
		}
		VipBasic vipBasic = UserVipService.getVipBasic(openId);
		if(vipBasic != null) {
			map.put("vipBasic", vipBasic);
		}
		map.put("mode", mode);
		map.put("sys_func", "user");
		
		return "user/page-user-index";
		
	}
	
	
	/**
	 * 根据OPENID获取用户基本信息
	 * @param openId
	 * @return
	 * @throws JSONException 
	 */
	@RequestMapping("/basic/get")
	@ResponseBody
	public Object getUserBasci(ModelMap map)   {
		String openId = (String) map.get("openId");
		JSONObject jsonRet = new JSONObject();
		UserBasic basic = UserVipService.getUserBasic(openId);
		if(basic != null) {//成功
			jsonRet.put("errcode", 0);
			jsonRet.put("errmsg", "ok");
			jsonRet.put("datas", JSONObject.toJSON(basic));
			map.put("userBasic", basic);
		}else {
			jsonRet.put("errcode", -1);
			jsonRet.put("errmsg", "获取用户基本信息失败！");
		}
		return jsonRet.toString();
	}

	/**
	 * 获取更新基本信息页面
	 * @return
	 */
	@RequestMapping("/basic/edit")
	public String editBasic() {
		
		return "user/page-basic-detail";
	}
	
	/**
	 * 将收到的修改请求发送至服务中心
	 * @param userBasic
	 * @param result
	 * @return
	 * @throws JSONException 
	 */
	@RequestMapping(value="/basic/update",method=RequestMethod.POST)
	@ResponseBody
	public String updateBasic(@Valid UserBasic basic,BindingResult result,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			//用户信息验证结果处理
			if(result.hasErrors()){
				StringBuilder sb = new StringBuilder();
				List<ObjectError> list = result.getAllErrors();
				for(ObjectError e :list){
					sb.append(e.getDefaultMessage());
				}
				jsonRet.put("errmsg", sb.toString());
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				return jsonRet.toString();
			}
			
			basic.setOpenId((String) map.get("openId"));
//			if(openId == null || openId.length()<6) {
//				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
//				jsonRet.put("errmsg", " openid: not null and length range is 6-100. ");
//				return jsonRet.toString();
//			}
			return UserVipService.updateUserBasic(basic);
		}catch(Exception e) {
			//数据处理
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
		
	}
	
	/**
	 * 获取用户推广信息
	 * 1、取已推广用户数量；
	 * 2、获取最新推广二维码；
	 * @param create		是否强制重新生成
	 * @param map
	 * @return
	 * @throws JSONException 
	 */
	@RequestMapping("/spread")
	public String getSpread(String create,ModelMap map)  {
		String openId = (String)map.get("openId");
		if(create != null && "1".equals(create.trim())){
			create = "1";
		}else {
			create = "0";
		}
		JSONObject ret = UserVipService.getSpreadQrCode(openId,create);
		if(ret.containsKey("errcode") && ret.getIntValue("errcode") == 0) {
			map.put("showurl", ret.getString("showurl"));
			map.put("count", ret.getIntValue("count"));
		}else {
			map.put("errmsg", ret.getString("errmsg"));
		}
		return "user/page-spread-qrcode";
	}
	
	/**
	 * 获取资金流水显示页面
	 * @param map
	 * @return
	 */
	@RequestMapping("/flow/show")
	public String showFlow(ModelMap map) {
		VipBasic vip = (VipBasic) map.get("vipBasic");
		if(vip == null || !"1".equals(vip.getStatus())) {
			map.put("errmsg", "您当前还未开通会员账户！");
		}
		
		return "user/page-show-change-flow";
	}
	
	
	/**
	 * 会员资金流水查询
	 * @param jsonParams  查询条件：{vipId,changeType, amountDown,amountUp,beginCrtTime,endCrtTime,beginSumTime,endSumTime,sumFlag,reason,createOpr}
	 * @param map
	 * @return {errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
	 */
	@RequestMapping("/flow/getall")
	@ResponseBody
	public String getAllFlow(String jsonParams,PageCond pageCond,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		VipBasic vip = (VipBasic) map.get("vipBasic");
		try {
			if(vip == null || !"1".equals(vip.getStatus())) {
				jsonRet.put("errcode", ErrCodes.VIP_NO_USER);
				jsonRet.put("errmsg", "您当前还未开通会员账户！");
				return jsonRet.toJSONString();
			}
			
			JSONObject params = JSONObject.parseObject(jsonParams);
			params.put("vipId", vip.getVipId());

			jsonRet = UserVipService.searchFlows(JSONObject.toJSONString(params), JSONObject.toJSONString(pageCond));
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_DB_ERROR);
				jsonRet.put("errmsg", "获取资金流水信息失败！");
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
}
