package com.mofangyouxuan.wx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
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
	 * @param mode 页面的主菜单：complain、suggest、about、joinus
	 * @param map
	 * @return
	 */
	@RequestMapping("/index/{mode}")
	public String getIndex(@PathVariable("mode")String mode,ModelMap map) {
		map.put("sys_func", "srvcenter");
		map.put("mode", mode);
		return "srvcenter/page-srvcenter-index";
	}
	
	/**
	 * 获取指定用户的所有投诉记录
	 * @return
	 */
	@RequestMapping("/complain/getall")
	public String getComplainAll(String userId) {
		
		return "srvcenter/page-complain-all";
	}
	
	/**
	 * 获取指定用户的所有建议记录
	 * @return
	 */
	@RequestMapping("/suggest/getall")
	public String getSuggestAll(String userId) {
		
		return "srvcenter/page-suggest-all";
	}	
	
	

}
