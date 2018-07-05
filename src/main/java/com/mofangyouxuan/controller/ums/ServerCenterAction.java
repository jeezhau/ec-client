package com.mofangyouxuan.controller.ums;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * 系统服务中心
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/srvc")
@SessionAttributes({"clientPF"})
public class ServerCenterAction {

	/**
	 * 获取关于我们的主页
	 * @return
	 */
	@RequestMapping("/about")
	public String getAbount(ModelMap map) {
		
		map.put("sys_func", "user");
		return "srvcenter/page-about-joinus";
	}
	
	
}
