package com.mofangyouxuan.wx.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.dto.UserBasic;
import com.mofangyouxuan.wx.utils.HttpUtils;

/**
 * 个人中心管理
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/user")
@SessionAttributes({"openId","vipBasic","userBasic","isDayFresh","sys_func"})
public class UserAction {
	
	@Value("${busi.mfyx-server-url}")
	private String mfyxServerUrl;
	
	@Value("${busi.user-basic-get-url}")
	private String userBasicGetUrl;
	
	@Value("${busi.user-basic-update-url}")
	private String userBasicUpdateUrl;
	
	@Value("${busi.qrcode-spread-url}")
	private String qrCodeSpreadUrl;
	
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	/**
	 * 用户管理主页面-我之基本
	 * @return
	 * @throws JSONException 
	 */
	@RequestMapping("/index/{mode}")
	public String getIndex(@PathVariable("mode")String mode,ModelMap map) throws JSONException {
		if(!"basic".equals(mode) && !"vip".equals(mode)) {
			mode = "basic";
		}
		map.put("mode", mode);
		map.put("sys_func", "user");
		return "page-user-index";
	}
	
	
	/**
	 * 根据OPENID获取用户基本信息
	 * @param openId
	 * @return
	 * @throws JSONException 
	 */
	@RequestMapping("/basic/get")
	@ResponseBody
	public String getUserBasci(ModelMap map) throws JSONException  {
		String openId = (String) map.get("openId");
		JSONObject jsonRet = new JSONObject();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("openId", openId);
		String strRet = HttpUtils.doPost(this.mfyxServerUrl + userBasicGetUrl, params);
		try {
			JSONObject ret = new JSONObject(strRet);
			if(ret.has("id")) {//成功
				jsonRet.put("errcode", 0);
				jsonRet.put("errmsg", "ok");
				jsonRet.put("datas", ret);
			}else {
				jsonRet = ret;
			}
		}catch(Exception e) {
			jsonRet.put("errcode", "-1");
			jsonRet.put("errmsg", "系统出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}

	/**
	 * 获取更新基本信息页面
	 * @return
	 */
	@RequestMapping("/basic/edit")
	public String editBasic() {
		
		return "page-user-basic";
	}
	
	/**
	 * 将收到的修改请求发送至服务中心
	 * @param userBasic
	 * @param result
	 * @return
	 * @throws JSONException 
	 */
	@RequestMapping(value="/basic/update",method=RequestMethod.POST)
	@ResponseBody
	public String updateBasic(@Valid UserBasic basic,BindingResult result) throws JSONException {
		JSONObject jsonRet = new JSONObject();
		try {
			//用户信息验证结果处理
			if(result.hasErrors()){
				StringBuilder sb = new StringBuilder();
				List<ObjectError> list = result.getAllErrors();
				for(ObjectError e :list){
					sb.append(e.getDefaultMessage());
				}
				jsonRet.put("errmsg", sb.toString());
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				return jsonRet.toString();
			}
			
			String openId = basic.getOpenId();
			if(openId == null || openId.length()<6) {
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", " openid: not null and length range is 6-100. ");
				return jsonRet.toString();
			}
			//数据重复性检查
			String registType = "2";
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("registType", registType);
			params.put("openId", openId);
			params.put("nickname", basic.getNickname());
			params.put("birthday", basic.getBirthday());
			params.put("phone", basic.getPhone());
			params.put("sex", basic.getSex());
			params.put("province", basic.getProvince());
			params.put("city", basic.getCity());
			params.put("favourite", basic.getFavourite());
			params.put("profession", basic.getProfession());
			params.put("introduce", basic.getIntroduce());
			String strRet = HttpUtils.doPost(this.mfyxServerUrl + this.userBasicUpdateUrl, params);
			return strRet;
		}catch(Exception e) {
			//数据处理
			jsonRet.put("errcode", ErrCodes.USER_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
		
	}
	
	/**
	 * 获取推广信息
	 * 1、取已推广用户数量；
	 * 2、获取最新推广二维码；
	 * @param create		是否强制重新生成
	 * @param map
	 * @return
	 * @throws JSONException 
	 */
	@RequestMapping("/spread")
	public String getSpread(String create,ModelMap map) throws JSONException {
		String openId = (String)map.get("openId");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("openId", openId);
		if(create != null && "1".equals(create.trim())){
			params.put("create", "1");
		}
		String strRet = HttpUtils.doPost(this.mfyxServerUrl+this.qrCodeSpreadUrl, params);
		JSONObject ret = new JSONObject(strRet);
		if(ret.has("errcode") && ret.getInt("errcode") == 0) {
			map.put("showurl", ret.getString("showurl"));
			map.put("count", ret.getInt("count"));
		}else {
			map.put("errmsg", ret.getString("errmsg"));
		}
		return "page-user-qrcode";
	}
}
