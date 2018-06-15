package com.mofangyouxuan.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.SysParam;
import com.mofangyouxuan.dto.UserBasic;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.service.UserService;
import com.mofangyouxuan.service.VipService;
import com.mofangyouxuan.utils.CommonUtil;
import com.mofangyouxuan.wxapi.WebAuth;




/**
 * 所有页面拦截器
 * @author jeekhan
 *
 */
public class AllInterceptor extends HandlerInterceptorAdapter{
	Logger log = LoggerFactory.getLogger(AllInterceptor.class);
	String serverName = SysParam.localServerName;
	
	/**
	 * 检查用户是否登陆，如果为登陆则返回登陆页面
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		HttpSession session = request.getSession();
		String uri = request.getRequestURI();
		//获取客户端平台
		String clientPF = (String) session.getAttribute("clientPF");
		if(clientPF == null || clientPF.length()<1) {
			CommonUtil.getPlatform(request);  //客户端平台
			session.setAttribute("clientPF", clientPF);
		}
		System.out.println("访问地址："+uri);
		UserBasic userBasic = (UserBasic) session.getAttribute("userBasic");
		VipBasic vipBasic = (VipBasic) session.getAttribute("vipBasic");
		//微信登录
		if(vipBasic == null || vipBasic.getVipId() == null) {
			String code = request.getParameter("code");
			String state = request.getParameter("state"); 
			if(code != null && code.length()>0 && state != null && WebAuth.STATE.equals(state)) {//从微信访问
				request.setAttribute("isFirstWxPage", "1"); //从微信进来的第一个页面
				//登录访问控制
				
				JSONObject auth = this.accessFromWX(code);
				if(auth == null) {
					response.sendRedirect(serverName + "/error/fromwx");
					return false;
				}
				String openId = null; //微信公众号OPENID、UNIONDID
				openId = auth.getString("openid");
				session.setAttribute("webAuth", auth);
				session.setAttribute("openId", openId);
				userBasic = UserService.getUserBasic(openId);
				if(userBasic == null) {
					response.sendRedirect(serverName + "/error/nouser");
					return false;
				}
				session.setAttribute("userBasic", userBasic);
				
				if(vipBasic == null || vipBasic.getVipId() == null) {
					vipBasic = VipService.getVipBasic(userBasic.getUserId());
					session.setAttribute("vipBasic", vipBasic);
				}
			}
		}
		//重定向首页访问至商城
		if(uri== null || uri.length()<0 || "/".equals(uri)) {
			response.sendRedirect(serverName + "/shop/index");
			return false;
		}
		
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
				userBasic.setRegistType("2");
				JSONObject ret = UserService.createUser(userBasic,"");
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
