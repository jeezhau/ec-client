package com.mofangyouxuan.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.dto.SettleAccount;
import com.mofangyouxuan.dto.UserBasic;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.service.VipService;
import com.mofangyouxuan.utils.PageCond;

/**
 * 会员管理
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/vip")
@SessionAttributes({"openId","userBasic","isDayFresh","sys_func"})
public class VipMgrAction {
	
	/**
	 * 获取资金流水显示页面
	 * @param map
	 * @return
	 */
	@RequestMapping("/flow/show")
	public String showFlow(@SessionAttribute("vipBasic") VipBasic vip,ModelMap map,HttpSession session) {
		if(vip == null || !"1".equals(vip.getStatus())) {
			map.put("errmsg", "您当前还未开通会员账户！");
		}
		
		return "vip/page-show-change-flow";
	}
	
	
	/**
	 * 会员资金流水查询
	 * @param jsonParams  查询条件：{vipId,changeType, amountDown,amountUp,beginCrtTime,endCrtTime,beginSumTime,endSumTime,sumFlag,reason,createOpr}
	 * @param map
	 * @return {errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
	 */
	@RequestMapping("/flow/getall")
	@ResponseBody
	public String getAllFlow(@SessionAttribute("vipBasic") VipBasic vip,String jsonParams,PageCond pageCond,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			if(vip == null || !"1".equals(vip.getStatus())) {
				jsonRet.put("errcode", ErrCodes.VIP_NO_USER);
				jsonRet.put("errmsg", "您当前还未开通会员账户！");
				return jsonRet.toJSONString();
			}
			
			JSONObject params = JSONObject.parseObject(jsonParams);
			params.put("vipId", vip.getVipId());

			jsonRet = VipService.searchFlows(JSONObject.toJSONString(params), JSONObject.toJSONString(pageCond));
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
	
	/**
	 * 获取账户设置首页
	 * @param map
	 * @return
	 */
	@RequestMapping("/vipset")
	public String getVipSetIndex(@SessionAttribute("vipBasic") VipBasic vip,ModelMap map,HttpSession session) {
		vip = VipService.getVipBasic(vip.getVipId());
		session.setAttribute("vipBasic", vip);
		if(vip == null || !"1".equals(vip.getStatus())) {
			map.put("errmsg", "您当前还未开通会员账户！");
		}
		map.put("sys_func", "user");
		return "vip/page-vipset-index";
	}
	
	
	
	/**
	 * 获取提现账户设置首页
	 * @param map
	 * @return
	 */
	@RequestMapping("/account/mgr")
	public String getAccountMgr(@SessionAttribute("vipBasic") VipBasic vip,ModelMap map) {
		if(vip == null || !"1".equals(vip.getStatus())) {
			map.put("errmsg", "您当前还未开通会员账户！");
		}
		
		return "vip/page-vipset-account";
	}
	
	
	/**
	 * 获取密码设置首页
	 * @param map
	 * @return
	 */
	@RequestMapping("/passwd/mgr")
	public String getPasswdMgr(@SessionAttribute("vipBasic") VipBasic vip,ModelMap map) {
		if(vip == null || !"1".equals(vip.getStatus())) {
			map.put("errmsg", "您当前还未开通会员账户！");
		}
		
		return "vip/page-vipset-passwd";
	}
	
	
	
	/**
	 * 重置会员密码
	 * @param pwd
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/vipset/resetpwd",method=RequestMethod.POST)
	@ResponseBody
	public String resetPwd(@SessionAttribute("vipBasic") VipBasic vip,
			@RequestParam(value="type",required=true)String type,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		UserBasic user = (UserBasic) map.get("userBasic");
		try {
			//数据验证
			type = type.trim();
			if(!type.matches("[12]")) {
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "密码的重置媒介类型不正确（1-手机，2-邮箱）！");
				return jsonRet.toString();
			}
			if(user == null || !"1".equals(user.getStatus()) || vip == null || !"1".equals(vip.getStatus())) {
				jsonRet.put("errcode", ErrCodes.USER_NO_EXISTS);
				jsonRet.put("errmsg", "系统中没有该会员用户！");
				return jsonRet.toString();
			}
			if("1".equals(type) && (user.getPhone() == null || user.getPhone().length()<11)){
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "您还未绑定手机号，请先完成手机号的绑定！");
				return jsonRet.toString();
			}
			if("2".equals(type) && (user.getEmail() == null || user.getEmail().length()<3)){
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "您还未绑定邮箱，请先完成邮箱的绑定！");
				return jsonRet.toString();
			}
			jsonRet = VipService.resetPwd(vip.getVipId(), type);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_DB_ERROR);
				jsonRet.put("errmsg", "更新会员密码信息失败！");
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	/**
	 * 更新会员密码
	 * @param pwd
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/vipset/updpwd",method=RequestMethod.POST)
	@ResponseBody
	public String updPasswd(@SessionAttribute("vipBasic") VipBasic vip,
			@RequestParam(value="oldPwd",required=true)String oldPwd,
			@RequestParam(value="newPwd",required=true)String newPwd,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			if(vip == null || !"1".equals(vip.getStatus())) {
				jsonRet.put("errcode", ErrCodes.VIP_NO_USER);
				jsonRet.put("errmsg", "系统中没有该会员用户！");
				return jsonRet.toJSONString();
			}
			oldPwd = oldPwd.trim();
			if(oldPwd.length()<6 || oldPwd.length()>20) {
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "原密码长度为6-20位字符！");
				return jsonRet.toString();
			}
			newPwd = newPwd.trim();
			if(newPwd.length()<6 || newPwd.length()>20) {
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "新密码长度为6-20位字符！");
				return jsonRet.toString();
			}
			jsonRet = VipService.updPwd(vip.getVipId(), oldPwd, newPwd);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_DB_ERROR);
				jsonRet.put("errmsg", "更新会员密码信息失败！");
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}

	/**
	 * 保存提现账户信息
	 * 
	 * @param account
	 * @param pwd
	 * @return
	 */
	@RequestMapping(value="/settle/save",method=RequestMethod.POST)
	@ResponseBody
	public Object saveAccount(@Valid SettleAccount account,BindingResult result,
			@RequestParam(value="passwd",required=true)String passwd,HttpSession session ) {
		JSONObject jsonRet = new JSONObject();
		VipBasic vip = (VipBasic) session.getAttribute("vipBasic");
		try {
			if(vip == null || !"1".equals(vip.getStatus())) {
				jsonRet.put("errcode", ErrCodes.VIP_NO_USER);
				jsonRet.put("errmsg", "您当前还未开通会员账户！");
				return jsonRet.toJSONString();
			}
			//数据格式验证
			if(result.hasErrors()){
				StringBuilder sb = new StringBuilder();
				List<ObjectError> list = result.getAllErrors();
				for(ObjectError e :list){
					sb.append(e.getDefaultMessage());
				}
				jsonRet.put("errmsg", sb.toString());
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				return jsonRet.toString();
			}
			passwd = passwd.trim();
			if(passwd.length()<6 || passwd.length()>20) {
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "密码长度为6-20位字符！");
				return jsonRet.toString();
			}
			account.setVipId(vip.getVipId());
			jsonRet = VipService.saveAccount(vip.getVipId(), account, passwd);
		}catch(Exception e) {
			//数据处理
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}

	/**
	 * 删除提现账户信息
	 * 
	 * @param settleId
	 * @param passwd
	 * @return
	 */
	@RequestMapping(value="/settle/delete",method=RequestMethod.POST)
	@ResponseBody
	public Object deleteAccount(@SessionAttribute("vipBasic") VipBasic vip,
			@RequestParam(value="settleId",required=true)Long settleId,
			@RequestParam(value="passwd",required=true)String passwd) {
		JSONObject jsonRet = new JSONObject();
		try {
			if(vip == null || !"1".equals(vip.getStatus())) {
				jsonRet.put("errcode", ErrCodes.VIP_NO_USER);
				jsonRet.put("errmsg", "您当前还未开通会员账户！");
				return jsonRet.toJSONString();
			}
			
			passwd = passwd.trim();
			if(passwd.length()<6 || passwd.length()>20) {
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "密码长度为6-20位字符！");
				return jsonRet.toString();
			}
			jsonRet = VipService.deleteAccount(vip.getVipId(), settleId, passwd);
		}catch(Exception e) {
			//数据处理
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	/**
	 * 获取所有账户信息
	 * 
	 * @return
	 */
	@RequestMapping(value="/settle/getall",method=RequestMethod.POST)
	@ResponseBody
	public Object getAllAccount(@SessionAttribute("vipBasic") VipBasic vip,
			@RequestParam(value="passwd",required=false)String passwd) {
		JSONObject jsonRet = new JSONObject();
		try {
			if(vip == null || !"1".equals(vip.getStatus())) {
				jsonRet.put("errcode", ErrCodes.VIP_NO_USER);
				jsonRet.put("errmsg", "您当前还未开通会员账户！");
				return jsonRet.toJSONString();
			}
			
			jsonRet = VipService.getAllAccount(vip.getVipId(), passwd);
		}catch(Exception e) {
			//数据处理
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	
}
