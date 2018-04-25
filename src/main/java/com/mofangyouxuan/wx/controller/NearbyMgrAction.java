package com.mofangyouxuan.wx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * 用户周边功能管理
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/nearby")
@SessionAttributes({"isDayFresh","sys_func"})
public class NearbyMgrAction {
	
	/**
	 * 获取周边功能首页
	 * @param map
	 * @return
	 */
	@RequestMapping("/index")
	public String getIndex(ModelMap map) {
		
		map.put("sys_func", "nearby");
		return "goods/page-nearby-index";
	}

}
