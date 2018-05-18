package com.mofangyouxuan.wx.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.dto.Category;
import com.mofangyouxuan.dto.PartnerBasic;
import com.mofangyouxuan.dto.UserBasic;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.service.GoodsService;
import com.mofangyouxuan.service.PartnerMgrService;
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
	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		//String contextPath = request.getContextPath();  
		HttpSession session = request.getSession();
		
		String openId = null;
		openId	= "ofvDZv893-8LL8qax8dyXGp2u23g";
		
		String code = request.getParameter("code");
		String state = request.getParameter("state"); 
		if(code != null && code.length()>0 && state != null && WebAuth.STATE.equals(state)) {//从微信访问
			JSONObject auth = this.accessFromWX(code);
			if(auth == null) {
				response.sendRedirect("/error/fromwx");
				return false;
			}
			openId = auth.getString("openid");
			session.setAttribute("webAuth", auth);
		}else { //使用其他方式访问
			
		}
		session.setAttribute("openId", openId);
		
		String isDayFresh = (String) request.getSession().getAttribute("isDayFresh");
		String sysFunc = (String) request.getSession().getAttribute("sys_func");
		if(isDayFresh == null) {
			session.setAttribute("isDayFresh", "0");
		}
		if(sysFunc == null) {
			session.setAttribute("sys_func", "");
		}
		
		UserBasic userBasic = (UserBasic) session.getAttribute("userBasic");
		VipBasic vipBasic = (VipBasic) session.getAttribute("vipBasic");
		PartnerBasic partnerBasic = (PartnerBasic) session.getAttribute("partnerBasic");
		if(userBasic == null || userBasic.getUserId() == null) {
			userBasic = UserService.getUserBasic(openId);
			if(userBasic == null) {
				response.sendRedirect("/error/nouser");
				return false;
			}
			session.setAttribute("userBasic", userBasic);
		}
		
		if(vipBasic == null || vipBasic.getVipId() == null) {
			vipBasic = VipService.getVipBasic(userBasic.getUserId());
			session.setAttribute("vipBasic", vipBasic);
		}
		if(partnerBasic == null) {
			partnerBasic = PartnerMgrService.getPartnerByVip(vipBasic.getVipId());
			session.setAttribute("partnerBasic", partnerBasic);
		}
		List<Category> categories = (List<Category>) session.getAttribute("categories");
		if(categories == null) {
			categories = GoodsService.getCategories();
			session.setAttribute("categories", categories);
		}
		
		//保存访问日志
		
        return true; 
	}
	
	private JSONObject accessFromWX(String code) {
		//获取授权信息
		JSONObject auth = WebAuth.getAccessToken(code, true, null);
		if(auth == null) {
			//response.sendRedirect("/error/fromwx");
			return null;
		}
		String openId = auth.getString("openid");
		//检查用户是否已经存在
		UserBasic userBasic = UserService.getUserBasic(openId);
		if(userBasic == null) {//发起注册
			userBasic = WebAuth.getUserInfo(auth);
			if(userBasic != null) {
				JSONObject ret = UserService.createUserBasic(userBasic);
				if(ret != null && ret.containsKey("errcode") && ret.getIntValue("errcode") ==0) {
					//用户信息注册成功
					;
				}else {//用户注册失败
					//response.sendRedirect("/error/fromwx");
					return null;
				}
			}else {//从微信获取用户失败
				//response.sendRedirect("/error/fromwx");
				return null;
			}
		}
		return auth;
	}
		
}
