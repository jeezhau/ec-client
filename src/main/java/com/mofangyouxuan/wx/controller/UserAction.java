package com.mofangyouxuan.wx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * 个人中心管理
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/user")
@SessionAttributes({"isDayFresh","sys_func"})
public class UserAction {
	
	/**
	 * 用户管理主页面-我之基本
	 * @return
	 */
	@RequestMapping("/index")
	public String getBasicIndex(ModelMap map) {
		
		map.put("sys_func", "user");
		return "page-user-index";
	}
	
	/**
	 * 用户管理主页面-我之基本
	 * @return
	 */
	@RequestMapping("/vip/get")
	public String getVipIndex() {
		
		return "page-myvip";
	}

}
