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
 * session管理拦截器
 * @author jeekhan
 *
 */
public class SessionInterceptor extends HandlerInterceptorAdapter{
	Logger log = LoggerFactory.getLogger(SessionInterceptor.class);
	
	
	/**
	 * 检查用户是否登陆，如果为登陆则返回登陆页面
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		//String contextPath = request.getContextPath();  
		HttpSession session = request.getSession();
		String uri = request.getRequestURI();
		//String agent= request.getHeader("user-agent").toLowerCase();
		//System.out.println(uri);
		//不需要控制session的路径
		String[] excluceUrl = {"/login","/image/file/show/","/partner/cert/show/","/user/headimg/show/",
				"/goods/show/","/goods/show/","/partner/mcht/","/srvcenter/index/"}; 
		String isDayFresh = (String) request.getSession().getAttribute("isDayFresh");
		String sysFunc = (String) request.getSession().getAttribute("sys_func");
		if(isDayFresh == null) {
			session.setAttribute("isDayFresh", "0");
		}
		if(sysFunc == null) {
			session.setAttribute("sys_func", "");
		}
		for(String exUri:excluceUrl) {
			if(uri.startsWith(exUri)) {
				return true;
			}
		}
		//登录访问控制
		UserBasic userBasic = (UserBasic) session.getAttribute("userBasic");
		VipBasic vipBasic = (VipBasic) session.getAttribute("vipBasic");
		if(vipBasic != null && vipBasic.getVipId() != null) {
			return true;
		}
		if(userBasic == null || userBasic.getUserId() == null) {
			session.setAttribute("fromUrl", uri);
			response.sendRedirect("/login");
			return false;
		}
		//保存访问日志
		
        return true; 
	}
	
}
