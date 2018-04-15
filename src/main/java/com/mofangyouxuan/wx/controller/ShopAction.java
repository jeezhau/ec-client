package com.mofangyouxuan.wx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * 商城管理
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/shop")
@SessionAttributes({"isDayFresh","sys_func"})
public class ShopAction {
	
	/**
	 * 获取商城管理主页
	 * @param map
	 * @return
	 */
	@RequestMapping("/index")
	public String getIndex(ModelMap map) {
		
		map.put("isDayFresh", "0");	//访问的每日鲜推，系统默认为访问商城管理
		map.put("sys_func", "shop");
		return "page-shop-index";
	}
	
}
