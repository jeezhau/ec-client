package com.mofangyouxuan.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.dto.UserBasic;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.service.UserService;
import com.mofangyouxuan.service.VipService;
import com.mofangyouxuan.utils.SignUtils;

@Controller
@SessionAttributes({"clientPF","userBasic","openId","vipBasic","fromUrl"})
public class LoginAction {
	@Value("${sys.local-server-url}")
	private String localServerName;
	
	private String regexpPhone = "1[3-9]\\d{9}";
	private String regexpEmail = "^[A-Za-z0-9_\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
	
	
	
	/**
	 * 用户登录
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/login")
	public String login(String username,String password,
			ModelMap map,HttpServletRequest request) {
		if(username != null && password != null) {
			UserBasic userBasic = UserService.getUserBasic(username);
			if(userBasic != null && "1".equals(userBasic.getStatus())) { //有正常用户
				try {
					if(SignUtils.encodeSHA256Hex(password).equals(userBasic.getPasswd())) {
						map.put("userBasic", userBasic);
						map.put("openId", username);
						VipBasic vipBasic = VipService.getVipBasic(userBasic.getUserId());
						if(vipBasic != null) {
							map.put("vipBasic", vipBasic);
						}
						String fromUrl = (String) map.get("fromUrl");
						if(fromUrl != null && fromUrl.length()>0) {
							return "redirect:" + localServerName + fromUrl;
						}else {
							return "redirect:" + localServerName + "/user/index/basic";
						}
					}else {
						map.put("username", username);
						map.put("errmsg", "用户名称与密码不正确！");
					}
				} catch (Exception e) {
					e.printStackTrace();
					map.put("errmsg", "出现系统异常，异常信息：" + e.getMessage());
				}
			}else {
				map.put("errmsg", "系统中没有该用户！" );
			}
		}
		return "user/page-login";
	}

	/**
	 * 用户登录
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/login-ajax")
	@ResponseBody
	public String loginAjax(String username,String password,
			ModelMap map,HttpServletRequest request) {
		JSONObject jsonRet = new JSONObject();
		if(username == null || password == null) {
			jsonRet.put("errcode", ErrCodes.COMMON_NO_LOGIN);
			jsonRet.put("errmsg", "用户未登录");
			return jsonRet.toJSONString();
		}
		try {
			UserBasic userBasic = UserService.getUserBasic(username);
			if(userBasic != null && "1".equals(userBasic.getStatus())) { //有正常用户
				if(SignUtils.encodeSHA256Hex(password).equals(userBasic.getPasswd())) {
					map.put("userBasic", userBasic);
					map.put("openId", username);
					VipBasic vipBasic = VipService.getVipBasic(userBasic.getUserId());
					if(vipBasic != null) {
						map.put("vipBasic", vipBasic);
					}
					jsonRet.put("errcode", 0);
					jsonRet.put("errmsg", "ok");
				}else {
					jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
					jsonRet.put("errmsg", "用户名称与密码不正确！");
				}
			}else {
				jsonRet.put("errcode", ErrCodes.USER_NO_EXISTS);
				jsonRet.put("errmsg", "系统中没有该用户！" );
			}
		}catch (Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现系统异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toJSONString();
	}
	
	/**
	 * 用户退出登录
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/logout")
	public String logout(
			ModelMap map,SessionStatus session){
		UserBasic user = (UserBasic) map.get("userBasic");
		if(user != null ){
			session.setComplete();	//清除用户session
		}
		return "redirect:" + localServerName+ "/shop/index";
	}
	
	/**
	 * 用户获取注册页面
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/register")
	public String register(ModelMap map){
		
		return "user/page-user-register";
	}
	
	/**
	 * 用户快速注册
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/register/quick")
	@ResponseBody
	public String registerQuick(@Valid UserBasic userBasic,BindingResult result,
			@RequestParam(value="media",required=true)String media,String veriCode) {
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
			String registType = "1";
			String email = userBasic.getEmail();
			String phone = userBasic.getPhone();
			String passwd = userBasic.getPasswd();
			if(email == null || phone == null) {
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "邮箱或手机号不可都为空！ ");
				return jsonRet.toString();
			}
			if(veriCode == null || veriCode.length() != 6) {
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "验证码：格式不正确！");
				return jsonRet.toString();
			}
			if("2".equals(media) ) {
				if( email == null && email.length()<1 || !email.matches(this.regexpEmail)) {
					jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
					jsonRet.put("errmsg", "邮箱：格式不正确！");
					return jsonRet.toString();
				}
				userBasic.setPhone("");
			}else {
				if(phone == null || phone.length()<1 || !phone.matches(this.regexpPhone)) {
					jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
					jsonRet.put("errmsg", "手机号：格式不正确！");
					return jsonRet.toString();
				}
				userBasic.setEmail("");
			}
			if(passwd == null || passwd.length()<6 || passwd.length()>20) {
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", " 密码: 长度 6-20 个字符. ");
				return jsonRet.toString();
			}
			userBasic.setRegistType(registType);
			jsonRet = UserService.createUser(userBasic, veriCode);
			if(!jsonRet.containsKey("errcode")) {
				jsonRet.put("errcode", -1);
				jsonRet.put("errmsg", "系统访问错误！");
			}
		}catch (Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现系统异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toJSONString();
	}
}
