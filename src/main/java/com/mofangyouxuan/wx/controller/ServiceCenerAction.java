package com.mofangyouxuan.wx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * 服务中心管理
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/srvcenter")
@SessionAttributes({"isDayFresh","sys_func"})
public class ServiceCenerAction {
	
	/**
	 * 获取服务中心主页
	 * @param map
	 * @return
	 */
	@RequestMapping("/index")
	public String getIndex(ModelMap map) {
		map.put("sys_func", "srvcenter");
		
		return "page-srvcenter-index";
	}

}
