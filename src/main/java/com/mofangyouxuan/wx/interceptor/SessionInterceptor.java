package com.mofangyouxuan.wx.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.mofangyouxuan.dto.Category;
import com.mofangyouxuan.dto.PartnerBasic;
import com.mofangyouxuan.dto.UserBasic;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.service.GoodsService;
import com.mofangyouxuan.service.PartnerMgrService;
import com.mofangyouxuan.service.UserVipService;




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
		
		String openId = (String) session.getAttribute("openId");
		if(openId == null) {
			openId = request.getParameter("openid");
			openId = "ofvDZv893-8LL8qax8dyXGp2u23g";
//			response.sendRedirect(contextPath + "/error/page-no-user.html");
//			return false;
		}
		
		String isDayFresh = (String) request.getSession().getAttribute("isDayFresh");
		String sysFunc = (String) request.getSession().getAttribute("sys_func");
		if(isDayFresh == null) {
			session.setAttribute("isDayFresh", "0");
		}
		if(sysFunc == null) {
			session.setAttribute("sys_func", "");
		}
		
		session.setAttribute("openId", openId);
		UserBasic userBasic = (UserBasic) session.getAttribute("userBasic");
		VipBasic vipBasic = (VipBasic) session.getAttribute("vipBasic");
		PartnerBasic partnerBasic = (PartnerBasic) session.getAttribute("partnerBasic");
		if(userBasic == null || userBasic.getUserId() == null) {
			userBasic = UserVipService.getUserBasic(openId);
			if(userBasic == null) {
				response.sendRedirect("/error/nouser");
				return false;
			}
			session.setAttribute("userBasic", userBasic);
		}
		
		if(vipBasic == null || vipBasic.getVipId() == null) {
			vipBasic = UserVipService.getVipBasic(openId);
			session.setAttribute("vipBasic", vipBasic);
		}
		if(partnerBasic == null) {
			partnerBasic = PartnerMgrService.getPartner(vipBasic.getVipId());
			session.setAttribute("partnerBasic", partnerBasic);
		}
		List<Category> categories = (List<Category>) session.getAttribute("categories");
		if(categories == null) {
			categories = GoodsService.getCategories();
			session.setAttribute("categories", categories);
		}
        return true; 
	}
		
}
