package com.mofangyouxuan.wx.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.mofangyouxuan.common.SysParam;
import com.mofangyouxuan.dto.PartnerStaff;
import com.mofangyouxuan.dto.VipBasic;




/**
 * session管理拦截器
 * @author jeekhan
 *
 */
public class PartnerSessionInterceptor extends HandlerInterceptorAdapter{
	Logger log = LoggerFactory.getLogger(PartnerSessionInterceptor.class);
	
	
	/**
	 * 检查用户是否登陆，如果为登陆则返回登陆页面
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String serverName = SysParam.localServerName;
		//String contextPath = request.getContextPath();  
		HttpSession session = request.getSession();
		String uri = request.getRequestURI();
		//String agent= request.getHeader("user-agent").toLowerCase();
		//System.out.println(uri);
		//不需要控制session的路径
		String[] excluceUrl = {"/partner/login"}; 

		for(String exUri:excluceUrl) {
			if(uri.startsWith(exUri)) {
				return true;
			}
		}
		//登录访问控制
		String partnerUserTP = (String) session.getAttribute("partnerUserTP");
		VipBasic partnerBindVip = (VipBasic) session.getAttribute("partnerBindVip");
		PartnerStaff partnerStaff = (PartnerStaff) session.getAttribute("partnerStaff");
		if(partnerUserTP == null) {
			session.setAttribute("fromUrl", uri);
			response.sendRedirect(serverName + "/partner/login");
			return false;
		}else if("bindVip".equals(partnerUserTP)) {
			if(partnerBindVip == null || partnerBindVip.getVipId() == null) {
				session.setAttribute("fromUrl", uri);
				response.sendRedirect(serverName + "/partner/login");
				return false;
			}
		}else {
			if(partnerStaff == null || partnerStaff.getPartnerId() == null) {
				session.setAttribute("fromUrl", uri);
				response.sendRedirect(serverName + "/partner/login");
				return false;
			}
		}
		//保存访问日志
        return true; 
	}
	
}


