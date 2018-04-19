package com.mofangyouxuan.wx.interceptor;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.mofangyouxuan.dto.UserBasic;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.wx.utils.HttpUtils;




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
		HttpSession session = request.getSession();
		String url = request.getRequestURI();
		String isDayFresh = (String) request.getSession().getAttribute("isDayFresh");
		String sysFunc = (String) request.getSession().getAttribute("sys_func");
		UserBasic userBasic = (UserBasic) request.getSession().getAttribute("userBasic");
		VipBasic vipBasic = (VipBasic) request.getSession().getAttribute("vipBasic");
		if(isDayFresh == null) {
			session.setAttribute("isDayFresh", "0");
		}
		if(sysFunc == null) {
			session.setAttribute("sys_func", "");
		}
		String openId = "ofvDZv893-8LL8qax8dyXGp2u23g";
		
		if(userBasic == null || userBasic.getId() == null) {
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("openId", openId);
			String strRet = HttpUtils.doPost("http://localhost:8080/mfyx/user/get", params);
			try {
				JSONObject ret = new JSONObject(strRet);
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				if(ret.has("id")) {//成功
					userBasic = new UserBasic();
					userBasic.setOpenId(openId);
					
					userBasic.setBirthday((ret.has("birthday") && !ret.isNull("birthday")) ? sdf1.parse(ret.getString("birthday")):null);
					userBasic.setCity((ret.has("city") && !ret.isNull("city")) ? ret.getString("city"):"");
					userBasic.setCountry((ret.has("country") && !ret.isNull("country")) ? ret.getString("country"):"");
					userBasic.setEmail((ret.has("email") && !ret.isNull("email")) ? ret.getString("email"):"");
					userBasic.setFavourite((ret.has("favourite") && !ret.isNull("favourite")) ? ret.getString("favourite"):"");
					userBasic.setHeadimgurl((ret.has("headimgurl") && !ret.isNull("headimgurl")) ? ret.getString("headimgurl"):"");
					userBasic.setId(ret.getInt("id"));
					userBasic.setIntroduce((ret.has("introduce") && !ret.isNull("introduce")) ? ret.getString("introduce"):"");
					userBasic.setNickname((ret.has("nickname") && !ret.isNull("nickname")) ? ret.getString("nickname"):"");
					userBasic.setOpenId(openId);
					userBasic.setPasswd("");
					userBasic.setPhone((ret.has("phone") && !ret.isNull("phone")) ? ret.getString("phone"):"");
					userBasic.setProfession((ret.has("profession") && !ret.isNull("profession")) ? ret.getString("profession"):"");
					userBasic.setProvince((ret.has("province") && !ret.isNull("province")) ? ret.getString("province"):"");
					//userBasic.setRegistTime(sdf2.parse(ret.getString("registTime")));
					userBasic.setRegistType(ret.getString("registType"));
					
					userBasic.setSenceId((ret.has("senceId") && !ret.isNull("senceId"))?ret.getInt("senceId"):null);
					userBasic.setSex((ret.has("sex") && !ret.isNull("sex")) ? ret.getString("sex"):"");
					userBasic.setStatus(ret.getString("status"));
					userBasic.setUnionId((ret.has("unionId") && !ret.isNull("unionId")) ? ret.getString("unionId"):"");
					//userBasic.setUpdateTime(sdf2.parse(ret.getString("updateTime")));
				}else {
					userBasic = null;
				}
			}catch(Exception e) {
				e.printStackTrace();
				userBasic = null;
			}
			session.setAttribute("userBasic", userBasic);
			session.setAttribute("openId", openId);
		}
		if(vipBasic == null || vipBasic.getVipId() == null) {
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("openId", openId);
			String strRetVip = HttpUtils.doPost("http://localhost:8080/mfyx/vip/get", params);
			try {
				JSONObject ret = new JSONObject(strRetVip);
				if(ret.has("vipId")) {//成功
					vipBasic = new VipBasic();
					vipBasic.setVipId(ret.getInt("vipId"));
					vipBasic.setBalance(new BigDecimal(ret.getDouble("balance")));
					vipBasic.setFreeze(new BigDecimal(ret.getDouble("freeze")));
					vipBasic.setIsPartner(ret.getString("isPartner"));
					vipBasic.setScores(ret.getInt("scores"));
					vipBasic.setStatus(ret.getString("status"));
				}
			}catch(Exception e) {
				e.printStackTrace();
				vipBasic = null;
			}
			session.setAttribute("vipBasic", vipBasic);
		}
        return true; 
	}
		
}
