package com.mofangyouxuan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 统一错误处理
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/error")
public class ErrorAction {
	
	/**
	 * 返回用户不存在错误页面
	 * @param map
	 * @return
	 */
	@RequestMapping("/nouser")
	public String noUser(ModelMap map) {
		
		return "error/page-no-user";
	}
	
	/**
	 * 微信访问错误页面
	 * @param map
	 * @return
	 */
	@RequestMapping("/fromwx")
	public String fromwx(ModelMap map) {
		
		return "error/page-fromwx-error";
	}
}
