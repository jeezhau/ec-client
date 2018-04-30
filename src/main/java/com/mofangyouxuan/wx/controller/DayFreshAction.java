package com.mofangyouxuan.wx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * 每日鲜推管理
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/dayfresh")
@SessionAttributes({"isDayFresh","sys_func"})
public class DayFreshAction {
	
	/**
	 * 返回主界面
	 * @param mode 界面首次打开显示的菜单，包括(today,tomorrow,history)
	 * @return
	 */
	@RequestMapping("/index/{mode}")
	public String getIndex(@PathVariable("mode")String mode,ModelMap map) {
		map.put("mode", mode);
		map.put("isDayFresh", "1");	//访问的每日鲜推，系统默认为访问商城管理
		map.put("sys_func", "dayfresh");
		
		return "shop/page-dayfresh-index";
	}
	
	

}
