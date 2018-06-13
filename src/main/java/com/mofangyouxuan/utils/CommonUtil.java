package com.mofangyouxuan.utils;

import javax.servlet.http.HttpServletRequest;

public class CommonUtil {
	
	/**
	 * 获取客户端类型
	 * @param request
	 * @return ios,apk,wx,pc
	 */
	public static String getPlatform(HttpServletRequest request){
	    /**User Agent中文名为用户代理，简称 UA，它是一个特殊字符串头，使得服务器
	    能够识别客户使用的操作系统及版本、CPU 类型、浏览器及版本、浏览器渲染引擎、浏览器语言、浏览器插件等*/  
	    String agent= request.getHeader("user-agent");
	    agent = agent.toLowerCase();
	    //客户端类型常量
	    String type = "";
	    if(agent.contains("iphone")||agent.contains("ipod")||agent.contains("ipad")){  
	        type = "ios";
	    } else if(agent.contains("android")) { 
	        type = "apk";
	    } else if(agent.indexOf("micromessenger") > 0){ 
	        type = "wx";
	    }else {
	        type = "pc";
	    }
	    return type;
	}
	
	/**
	 * 获取用户IP地址
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

}
