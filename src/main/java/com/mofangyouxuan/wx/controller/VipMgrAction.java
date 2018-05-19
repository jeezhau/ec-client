package com.mofangyouxuan.wx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.dto.UserBasic;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.service.VipService;
import com.mofangyouxuan.wx.utils.PageCond;

/**
 * 会员管理
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/vip")
@SessionAttributes({"openId","vipBasic","userBasic","isDayFresh","sys_func"})
public class VipMgrAction {
	
	private String regexpPhone = "1[3-9]\\d{9}";
	private String regexpEmail = "^[A-Za-z0-9_\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
	
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
	public String getVipSetIndex(ModelMap map) {
		UserBasic user = (UserBasic) map.get("userBasic");
		VipBasic vipBasic = VipService.getVipBasic(user.getUserId());
		if(vipBasic != null) {
			map.put("vipBasic", vipBasic);
		}
		if(vipBasic == null || !"1".equals(vipBasic.getStatus())) {
			map.put("errmsg", "您当前还未开通会员账户！");
		}
		map.put("sys_func", "user");
		return "vip/page-vipset-index";
	}
	
	/**
	 * 获取手机设置首页
	 * @param map
	 * @return
	 */
	@RequestMapping("/phone/mgr")
	public String getPhoneMgr(ModelMap map) {
		VipBasic vip = (VipBasic) map.get("vipBasic");
		if(vip == null || !"1".equals(vip.getStatus())) {
			map.put("errmsg", "您当前还未开通会员账户！");
		}
		
		return "vip/page-vipset-phone";
	}

	
	/**
	 * 获取邮箱设置首页
	 * @param map
	 * @return
	 */
	@RequestMapping("/email/mgr")
	public String getEmailMgr(ModelMap map) {
		VipBasic vip = (VipBasic) map.get("vipBasic");
		if(vip == null || !"1".equals(vip.getStatus())) {
			map.put("errmsg", "您当前还未开通会员账户！");
		}
		
		return "vip/page-vipset-email";
	}
	
	
	/**
	 * 获取提现设置首页
	 * @param map
	 * @return
	 */
	@RequestMapping("/account/mgr")
	public String getAccountMgr(ModelMap map) {
		VipBasic vip = (VipBasic) map.get("vipBasic");
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
	public String getPasswdMgr(ModelMap map) {
		VipBasic vip = (VipBasic) map.get("vipBasic");
		if(vip == null || !"1".equals(vip.getStatus())) {
			map.put("errmsg", "您当前还未开通会员账户！");
		}
		
		return "vip/page-vipset-passwd";
	}
	
	/**
	 * 更新绑定手机号
	 * @param oldVeriCode
	 * @param newPhone
	 * @param newVeriCode
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/vipset/updphone",method=RequestMethod.POST)
	@ResponseBody
	public Object updPhone(String oldVeriCode,
			@RequestParam(value="newPhone",required=true)String newPhone,
			@RequestParam(value="newVeriCode",required=true)String newVeriCode,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		VipBasic vip = (VipBasic) map.get("vipBasic");
		try {
			if(vip == null || !"1".equals(vip.getStatus())) {
				jsonRet.put("errcode", ErrCodes.VIP_NO_USER);
				jsonRet.put("errmsg", "您当前还未开通会员账户！");
				return jsonRet.toJSONString();
			}
			//数据格式验证
			if(vip.getPhone() != null && vip.getPhone().length()>=11) {
				if(oldVeriCode == null) {
					oldVeriCode = "";
				}
				oldVeriCode = oldVeriCode.trim();
				if(oldVeriCode.length() != 6 ) {
					jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
					jsonRet.put("errmsg", "原手机号短信验证码为6位字符！");
					return jsonRet.toString();
				}
			}
			newPhone = newPhone.trim();
			if(!newPhone.matches(regexpPhone)) {
				jsonRet.put("errcode","-1");
				jsonRet.put("errmsg","新手机号格式不正确！");
				return jsonRet;
			}
			newVeriCode = newVeriCode.trim();
			if(newVeriCode.length() != 6 ) {
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "新手机号短信验证码为6位字符！");
				return jsonRet.toString();
			}
			jsonRet = VipService.updPhone(vip.getVipId(), oldVeriCode, newPhone, newVeriCode);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_DB_ERROR);
				jsonRet.put("errmsg", "绑定手机号失败！");
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	/**
	 * 更新绑定邮箱
	 * @param oldVeriCode
	 * @param newEmail
	 * @param newVeriCode
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/vipset/updemail",method=RequestMethod.POST)
	@ResponseBody
	public Object updEmail(String oldVeriCode,
			@RequestParam(value="newEmail",required=true)String newEmail,
			@RequestParam(value="newVeriCode",required=true)String newVeriCode,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		VipBasic vip = (VipBasic) map.get("vipBasic");
		try {
			if(vip == null || !"1".equals(vip.getStatus())) {
				jsonRet.put("errcode", ErrCodes.VIP_NO_USER);
				jsonRet.put("errmsg", "您当前还未开通会员账户！");
				return jsonRet.toJSONString();
			}
			//数据格式验证
			if(vip.getEmail() != null && vip.getEmail().length()>=3) {
				if(oldVeriCode == null) {
					oldVeriCode = "";
				}
				oldVeriCode = oldVeriCode.trim();
				if(oldVeriCode.length() != 6 ) {
					jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
					jsonRet.put("errmsg", "原邮箱验证码为6位字符！");
					return jsonRet.toString();
				}
			}
			newEmail = newEmail.trim();
			if(!newEmail.matches(regexpEmail)) {
				jsonRet.put("errcode","-1");
				jsonRet.put("errmsg","新邮箱格式不正确！");
				return jsonRet;
			}
			newVeriCode = newVeriCode.trim();
			if(newVeriCode.length() != 6 ) {
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "新邮箱验证码为6位字符！");
				return jsonRet.toString();
			}
			jsonRet = VipService.updEmail(vip.getVipId(), oldVeriCode, newEmail, newVeriCode);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_DB_ERROR);
				jsonRet.put("errmsg", "绑定邮箱失败！");
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	/**
	 * 更新资金操作密码
	 * @param pwd
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/vipset/updpwd",method=RequestMethod.POST)
	@ResponseBody
	public String updPwd(@RequestParam(value="oldPwd",required=true)String oldPwd,
			@RequestParam(value="newPwd",required=true)String newPwd,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		VipBasic vip = (VipBasic) map.get("vipBasic");
		try {
			if(vip == null || !"1".equals(vip.getStatus())) {
				jsonRet.put("errcode", ErrCodes.VIP_NO_USER);
				jsonRet.put("errmsg", "您当前还未开通会员账户！");
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
			jsonRet = VipService.updPayPwd(vip.getVipId(), oldPwd, newPwd);
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
	 * 重置会员密码
	 * @param pwd
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/vipset/resetpwd",method=RequestMethod.POST)
	@ResponseBody
	public String resetPwd(@RequestParam(value="type",required=true)String type,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		VipBasic vip = (VipBasic) map.get("vipBasic");
		try {
			//数据验证
			type = type.trim();
			if(!type.matches("[12]")) {
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "密码的重置媒介类型不正确（1-手机，2-邮箱）！");
				return jsonRet.toString();
			}
			if(vip == null) {
				jsonRet.put("errcode", ErrCodes.USER_NO_EXISTS);
				jsonRet.put("errmsg", "系统中没有该会员用户！");
				return jsonRet.toString();
			}
			if("1".equals(type) && (vip.getPhone() == null || vip.getPhone().length()<11)){
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "您还未绑定手机号，请先完成手机号的绑定！");
				return jsonRet.toString();
			}
			if("2".equals(type) && (vip.getEmail() == null || vip.getEmail().length()<3)){
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
	 * 更新会员提现信息
	 * 
	 * @param vipId
	 * @param idNo
	 * @param actNm
	 * @param actNo
	 * @param actBlk
	 * @param pwd
	 * @return
	 */
	@RequestMapping(value="/vipset/updact",method=RequestMethod.POST)
	@ResponseBody
	public Object updAccount(@RequestParam(value="cashTp",required=true)String cashTp,
			@RequestParam(value="actTp",required=true)String actTp,
			@RequestParam(value="idNo",required=true)String idNo,
			@RequestParam(value="actNm",required=true)String actNm,
			@RequestParam(value="actNo",required=true)String actNo,
			@RequestParam(value="actBlk",required=true)String actBlk,
			@RequestParam(value="pwd",required=true)String pwd,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		VipBasic vip = (VipBasic) map.get("vipBasic");
		try {
			if(vip == null || !"1".equals(vip.getStatus())) {
				jsonRet.put("errcode", ErrCodes.VIP_NO_USER);
				jsonRet.put("errmsg", "您当前还未开通会员账户！");
				return jsonRet.toJSONString();
			}
			//数据格式验证
			cashTp = cashTp.trim();
			actTp =actTp.trim();
			idNo = idNo.trim();
			actNm = actNm.trim();
			actNo = actNo.trim();
			actBlk = actBlk.trim();
			if(!cashTp.matches("[123]")) {
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "提现方式格式不正确！");
				return jsonRet.toString();
			}
			if(!actTp.matches("[12]")) {
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "账户类型格式不正确！");
				return jsonRet.toString();
			}
			if(!idNo.matches("[1-9]\\d{16}[0-9Xx]")) {
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "身份证号码格式不正确！");
				return jsonRet.toString();
			}
			if(actNm.length()<2 || actNm.length()>100) {
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "账户名长度为2-100位字符！");
				return jsonRet.toString();
			}
			if(actNo.length()<3 || actNo.length()>30) {
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "账户号长度为3-30位字符！");
				return jsonRet.toString();
			}
			if(actBlk.length()<2 || actBlk.length()>100) {
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "开户行名称长度为2-100位字符！");
				return jsonRet.toString();
			}
			pwd = pwd.trim();
			if(pwd.length()<6 || pwd.length()>20) {
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "密码长度为6-20位字符！");
				return jsonRet.toString();
			}
			jsonRet = VipService.updAccount(vip.getVipId(), cashTp, actTp, idNo, actNm, actNo, actBlk, pwd);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_DB_ERROR);
				jsonRet.put("errmsg", "更新账户信息失败！");
			}
		}catch(Exception e) {
			//数据处理
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}

	/**
	 * 申请手机短信验证码
	 * @param phone
	 * @return
	 */
	@RequestMapping(value="/vericode/phone/apply",method=RequestMethod.POST)
	@ResponseBody
	public Object applyPhoneVeriCode(@RequestParam(value="phone",required=true)String phone,
			ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			VipBasic vip = (VipBasic) map.get("vipBasic");
			if(vip == null || !"1".equals(vip.getStatus())) {
				jsonRet.put("errmsg", "您当前还未开通会员账户！");
				jsonRet.put("errcode", -1);
				return jsonRet;
			}
			if(!phone.matches(regexpPhone)) {
				jsonRet.put("errcode","-1");
				jsonRet.put("errmsg","手机号格式不正确！");
				return jsonRet;
			}
			jsonRet = VipService.getPhoneVeriCode(phone);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_DB_ERROR);
				jsonRet.put("errmsg", "获取短信验证码失败！");
			}
		}catch(Exception e) {
			//数据处理
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	/**
	 * 申请邮箱短信验证码
	 * @param email
	 * @return
	 */
	@RequestMapping(value="/vericode/email/apply",method=RequestMethod.POST)
	@ResponseBody
	public Object applyEmailVeriCode(@RequestParam(value="email",required=true)String email,
			ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			VipBasic vip = (VipBasic) map.get("vipBasic");
			if(vip == null || !"1".equals(vip.getStatus())) {
				jsonRet.put("errmsg", "您当前还未开通会员账户！");
				jsonRet.put("errcode", -1);
				return jsonRet;
			}
			if(!email.matches(regexpEmail)) {
				jsonRet.put("errcode","-1");
				jsonRet.put("errmsg","邮箱格式不正确！");
				return jsonRet;
			}
			jsonRet = VipService.getEmialVeriCode(email);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_DB_ERROR);
				jsonRet.put("errmsg", "获取邮箱验证码失败！");
			}
		}catch(Exception e) {
			//数据处理
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
}
