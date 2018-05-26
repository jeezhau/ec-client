package com.mofangyouxuan.wx.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.dto.UserBasic;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.service.UserService;
import com.mofangyouxuan.service.VipService;
import com.mofangyouxuan.wx.api.WebAuth;




/**
 * 所有页面拦截器
 * @author jeekhan
 *
 */
public class AllInterceptor extends HandlerInterceptorAdapter{
	Logger log = LoggerFactory.getLogger(AllInterceptor.class);
	
	
	/**
	 * 检查用户是否登陆，如果为登陆则返回登陆页面
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		//HttpSession session = request.getSession();
		String uri = request.getRequestURI();
		//String agent= request.getHeader("user-agent").toLowerCase();
		if(uri== null || uri.length()<0 || "/".equals(uri)) {
			response.sendRedirect("/shop/index");
			return false;
		}
        return true; 
	}
	
	
		
}
