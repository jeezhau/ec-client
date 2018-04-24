package com.mofangyouxuan.wx.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.common.SysConfigParam;
import com.mofangyouxuan.dto.PartnerBasic;
import com.mofangyouxuan.dto.UserBasic;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.service.PartnerMgrService;
import com.mofangyouxuan.service.WXMPService;

/**
 * 合作伙伴管理
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/partner")
@SessionAttributes({"openId","vipBasic","userBasic","partnerBasic"})
public class PartnerAction {
	@Value("${sys.local-server-url}")
	private String localServerUrl;
	
	/**
	 * 获取合作伙伴管理首页
	 * @return
	 */
	@RequestMapping("/index")
	public String getIndex(ModelMap map) {
		VipBasic vipBasic = (VipBasic) map.get("vipBasic");
		if(vipBasic == null) {
			return "error/page-no-user";
		}else if(!"1".equals(vipBasic.getStatus())) {
			map.put("errmsg", "您尚未激活会员账户功能！")	;
			return "forward:/user/index/vip" ;
		}
		
		return "partner/page-partner-index";
	}
	
	/**
	 * 获取合作信息编辑页面
	 * @return
	 */
	@RequestMapping("/edit")
	public String basicEdit(ModelMap map) {
		VipBasic vipBasic = (VipBasic) map.get("vipBasic");
		if(vipBasic == null) {
			return "error/page-no-user";
		}else if(!"1".equals(vipBasic.getStatus())) {
			map.put("errmsg", "您尚未激活会员账户功能！")	;
			return "forward:/user/index/vip" ;
		}
		map.put("APP_ID", SysConfigParam.APP_ID);
		String nonceStr = "wddgwefw";
		Long timestamp = System.currentTimeMillis();
		String url = localServerUrl + "/partner/edit";
		String signature = "";
		try {
			JSONObject json = WXMPService.getSignature(url, timestamp, nonceStr);
			if(json.containsKey("signature")) {
				signature = json.getString("signature");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		map.put("nonceStr", nonceStr);
		map.put("timestamp", timestamp);
		map.put("signature", signature);
		
		return "partner/page-partner-detail";
	}
	
	/**
	 * 根据合作伙伴绑定的用户获取合作伙伴信息
	 * 
	 * @return {errcode:-1,errmsg:"错误信息"} 或 {合作伙伴的所有字段}
	 * @throws JSONException 
	 */
	@RequestMapping("/get")
	@ResponseBody
	public Object getPartner(ModelMap map){
		JSONObject jsonRet = new JSONObject();
		VipBasic vip = (VipBasic)map.get("vipBasic");
		if(vip == null || !"1".equals(vip.getStatus()) ) {
			jsonRet.put("errcode", ErrCodes.VIP_NO_USER);
			jsonRet.put("errmsg", "系统中没有该会员或未激活！");
			return jsonRet.toString();
		}
		
		if(!"1".equals(vip.getIsPartner())) {
			jsonRet.put("errcode", 0);
			jsonRet.put("errmsg", "您还没开通合作伙伴功能！");
			return jsonRet.toString();
		}else {
			PartnerBasic partner = null;
			try{
				partner = PartnerMgrService.getPartner(vip.getVipId());
			}catch(Exception e) {
				e.printStackTrace();
			}
			if(partner == null) {
				jsonRet.put("errcode", ErrCodes.PARTNER_NO_EXISTS);
				jsonRet.put("errmsg", "获取合作伙伴信息失败！");
				return jsonRet.toString();
			}
			return partner;
		}
	}


	/**
	 * 开通创建合作伙伴
	 * @param basic	合作伙伴信息
	 * @param result 字段验证结果
	 * 
	 * @return {errcode:0,errmsg:"ok"} 
	 * @throws JSONException
	 */
	@RequestMapping(value="/create",method=RequestMethod.POST)
	public String create(@Valid PartnerBasic basic,BindingResult result,ModelMap map)  {
		JSONObject jsonRet = new JSONObject();
		UserBasic user = (UserBasic)map.get("userBasic");
		VipBasic vip = (VipBasic)map.get("vipBasic");
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

			//数据检查
			if(!"1".equals(vip.getStatus()) ) {
				jsonRet.put("errcode", ErrCodes.VIP_NO_USER);
				jsonRet.put("errmsg", "系统中没有该会员或未激活！");
				return jsonRet.toString();
			}
			
			//数据处理
			basic.setUserId(user.getId());
			String ret = PartnerMgrService.create(basic);
			JSONObject retObj = JSONObject.parseObject(ret);
			if(retObj.containsKey("partnerId")) {
				basic.setId(retObj.getInteger("partnerId"));
				basic.setStatus("0");
				map.put("partnerBasic", basic);
			}
			return ret;
		}catch(Exception e) {
			//数据处理
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	
	/**
	 * 更新合作伙伴
	 * 
	 * @param basic	合作伙伴信息
	 * @param result 字段验证结果
	 * 
	 * @return {errcode:0,errmsg:"ok"}
	 * @throws JSONException 
	 */
	@RequestMapping(value="/update",method=RequestMethod.POST)
	public String update(@Valid PartnerBasic basic,BindingResult result,ModelMap map){
		JSONObject jsonRet = new JSONObject();
		UserBasic user = (UserBasic)map.get("userBasic");
		PartnerBasic partner = (PartnerBasic)map.get("partnerBasic");
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
			//数据检查
			if(partner == null ) {
				jsonRet.put("errcode", ErrCodes.PARTNER_PARAM_ERROR);
				jsonRet.put("errmsg", "您还未开通合作伙伴！");
				return jsonRet.toString();
			}
			//数据处理
			basic.setId(partner.getId());
			basic.setUserId(user.getId());
			basic.setReviewLog("");
			basic.setReviewOpr(null);
			basic.setReviewTime(null);
			basic.setCertDir(null); 
			basic.setUpdateTime(null);
			
			String strRet = PartnerMgrService.update(basic);
			JSONObject retObj = JSONObject.parseObject(strRet);
			if(retObj.containsKey("errcode") && retObj.getIntValue("errcode") == 0) {
				basic.setStatus("0");
				map.put("partnerBasic", basic);
			}
			return strRet;
		}catch(Exception e) {
			//数据处理
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	/**
	 * 变更合作伙伴状态：关闭、打开
	 * 
	 * @return {errcode:0,errmsg:"ok"}
	 * @throws JSONException 
	 */
	@RequestMapping("/changeStatus")
	public String changeStatus(ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			PartnerBasic old = (PartnerBasic) map.get("partnerBasci");
			//数据检查
			if(old == null ) {
				jsonRet.put("errcode", ErrCodes.PARTNER_PARAM_ERROR);
				jsonRet.put("errmsg", "您还未开通合作伙伴！");
				return jsonRet.toString();
			}
			String oldStatus = old.getStatus();
			if(!"S".equals(oldStatus) && !"C".equals(oldStatus)) { //正常或关闭
				jsonRet.put("errcode", ErrCodes.PARTNER_STATUS_ERROR);
				jsonRet.put("errmsg", "您不可变更当前状态！！");
				return jsonRet.toString();
			}
			String newStatus = "";
			if("S".equals(oldStatus)) {
				newStatus = "C";
			}else {
				newStatus = "S";
			}
			String strRet = PartnerMgrService.changeStatus(old.getId(), old.getUserId());
			JSONObject retObj = JSONObject.parseObject(strRet);
			if(retObj.containsKey("errcode") && retObj.getIntValue("errcode") == 0) {
				old.setStatus(newStatus);
				map.put("partnerBasic", old);
			}
			return strRet;
		}catch(Exception e) {
			//数据处理
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
}
