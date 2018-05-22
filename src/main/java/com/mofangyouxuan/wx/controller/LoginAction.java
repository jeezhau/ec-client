package com.mofangyouxuan.wx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.mofangyouxuan.dto.UserBasic;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.service.UserService;
import com.mofangyouxuan.service.VipService;
import com.mofangyouxuan.wx.utils.SignUtils;

@Controller
@SessionAttributes({"userBasic","openId","vipBasic","fromUrl"})
public class LoginAction {
	
	/**
	 * 用户登录
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/login")
	public String login(String username,String password,
			ModelMap map) {
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
							return "redirect:" + fromUrl;
						}else {
							return "redirect:/user/index/basic";
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
		return "redirect:/shop/index";
	}
	
}
